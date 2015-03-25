package com.prisadigital.realmedia.adlib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

public class GifDecoderFactory {
    // Raw file data and buffers
    private byte                        mGifPointer[];
    private byte                        mGifBuffer[];
    private byte                        mGifScreen[];
    private byte                        mGifGlobal[];
    // Frames
    private ArrayList<AnimatedGifFrame> mFrames;
    // Colors
    private int                         mGifSorted;
    private int                         mGifColorS;
    private int                         mGifColorC;
    private int                         mGifColorF;

    // Pointer Index
    private int                         mDataPointer;

    // Animation length
    @SuppressWarnings("unused")
    private long                        mAnimationLength;

    // Private Constructor
    private GifDecoderFactory(byte[] data) {
        // Save Data
        this.mGifPointer = data;
    }

    /*
     * private int getNumFrameByTime(long time) { long currentTime; int numFrame, currentFrame;
     * 
     * if (mFrames.size() <= 0) return -1;
     * 
     * if (mFrames.size()== 0) return 0;
     * 
     * if (time > mAnimationLength) time = time % mAnimationLength;
     * 
     * // Initialize currentTime = (int)mFrames.get(0).delay; currentFrame = 0; numFrame = 0;
     * 
     * // Iterate while(currentTime <= mAnimationLength && currentFrame < mFrames.size()) {
     * AnimatedGifFrame frame = mFrames.get(currentFrame);
     * 
     * // Check if the time is within frame delay if (time <= currentTime) { // Save Frame and exit
     * numFrame = currentFrame; break; }
     * 
     * // Move to the next frame currentTime += frame.delay; currentFrame ++; }
     * 
     * return numFrame; }
     */

    private void prepareFramesToDraw() {
        // Not prepare if there are not frames
        if (mFrames.size() <= 0)
            return;

        // Calculate image size (first frame)
        int width = mFrames.get(0).gifFrame.getWidth();
        int height = mFrames.get(0).gifFrame.getHeight();

        // Create Paint
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(false);

        // Create Bitmap and Canvas to work
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // Create canvas from the bitmap
        Canvas c = new Canvas(bitmap);

        // Iterate All Frames
        for (AnimatedGifFrame frame : mFrames) {
            // Create the mutable bitmap
            Bitmap frameBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // Create canvas from the bitmap
            Canvas frameCanvas = new Canvas(frameBitmap);

            switch (frame.disposalMethod) {
                case 1: // Do not dispose (draw over context)
                default:
                    // Create Rect (y inverted) to clipping
                    c.clipRect(frame.area.left, frame.area.top, frame.area.left + frame.area.right,
                            frame.area.top + frame.area.bottom, Region.Op.REPLACE);
                    // Draw Frame in working canvas
                    c.drawBitmap(frame.gifFrame, 0, 0, null);
                    // Dump canvas to frame
                    frameCanvas.drawBitmap(bitmap, 0, 0, null);
                    break;
                case 2: // Restore to background the rect when the actual frame will go to be drawed
                    // Create Rect to clipping
                    c.clipRect(frame.area.left, frame.area.top, frame.area.left + frame.area.right,
                            frame.area.top + frame.area.bottom, Region.Op.REPLACE);
                    // Draw frame portion
                    c.drawBitmap(frame.gifFrame, 0, 0, null);
                    // Dump canvas to frame
                    frameCanvas.drawBitmap(bitmap, 0, 0, null);

                    // Clear the old zone (first, we need to prepare the paint)
                    paint.setColor(Color.TRANSPARENT);
                    paint.setStyle(Style.FILL);
                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                    // Draw the rect
                    c.drawRect(frame.area.left, frame.area.top, frame.area.left + frame.area.right,
                            frame.area.top + frame.area.bottom, paint);
                    break;
                case 3: // Restore to Previous (To get it we paint on the bitmapFrame instead of
                        // working canvas)
                    frameCanvas.drawBitmap(bitmap, 0, 0, null);
                    // Create Rect (y inverted) to clipping
                    frameCanvas.clipRect(frame.area.left, frame.area.top, frame.area.left
                            + frame.area.right, frame.area.top + frame.area.bottom,
                            Region.Op.REPLACE);
                    // Draw frame portion
                    frameCanvas.drawBitmap(frame.gifFrame, 0, 0, null);
                    break;
            }

            // Save Full frame
            frame.fullFrame = frameBitmap;

            // Free gif frame (we have the full frame so we don't need the gif portion)
            frame.gifFrame = null;
        }
    }

    private AnimationDrawable createAnimationDrawable() {
        // Create Object
        AnimationDrawable ad = new AnimationDrawable();
        // Set infinie
        ad.setOneShot(false);

        // Iterate Frames
        for (AnimatedGifFrame frame : mFrames) {
            if (frame.delay > 0 || mFrames.size() == 1) {
                // Create Drawable
                BitmapDrawable bd = new BitmapDrawable(frame.fullFrame);
                // Add to animation
                ad.addFrame(bd, (int) frame.delay);
            }
        }

        // Return animation
        return ad;
    }

    private void decodeGif(byte data[]) throws IOException {
        // Save Raw Data File
        mGifPointer = data;

        // Initialize Frames
        mFrames = new ArrayList<AnimatedGifFrame>();
        // Initialize Pointer
        mDataPointer = 0;

        // Skip "GIF89a"
        skipGifBytes(6);
        // Copy Logical Screen Descriptor to mGifBuffer
        getGifBytes(7);

        // Deep copy
        mGifScreen = new byte[mGifBuffer.length];
        System.arraycopy(mGifBuffer, 0, mGifScreen, 0, mGifBuffer.length);

        // Copy the read bytes into a local buffer on the stack
        // For easy byte access in the following lines.
        int length = mGifBuffer.length;
        byte aBuffer[] = new byte[length];
        System.arraycopy(mGifBuffer, 0, aBuffer, 0, length);

        // Check Color
        if ((aBuffer[4] & 0x80) != 0x00) {
            mGifColorF = 1;
        } else {
            mGifColorF = 0;
        }

        if ((aBuffer[4] & 0x08) != 0x00) {
            mGifSorted = 1;
        } else {
            mGifSorted = 0;
        }

        mGifColorC = (aBuffer[4] & 0x07);
        mGifColorS = 2 << mGifColorC;

        if (mGifColorF == 1) {
            getGifBytes(3 * mGifColorS);

            // Deep copy
            mGifGlobal = new byte[mGifBuffer.length];
            System.arraycopy(mGifBuffer, 0, mGifGlobal, 0, mGifGlobal.length);
        }

        byte bBuffer;
        while (getGifBytes(1) == true) {
            // Copy first char every time
            bBuffer = mGifBuffer[0];

            // Check if the char mark the end
            if (bBuffer == 0x3B) {
                break;
            }

            switch (bBuffer) {
                case 0x21:
                    // Graphic Control Extension (#n of n)
                    readGifExtensions();
                    break;
                case 0x2C:
                    // Image Descriptor (#n of n)
                    readGifDescriptor();
                    break;
            }
        }

        // Free memory
        mGifScreen = null;
        mGifBuffer = null;
        mGifGlobal = null;
    }

    private void readGifExtensions() throws IOException {
        // 21! But we still could have an Application Extension,
        // so we want to check for the full signature.
        int cur, prev;

        getGifBytes(1);
        cur = mGifBuffer[0] & 0xFF;
        prev = 0;

        while (cur != 0x00) {
            // TODO: Known bug, the sequence F9 04 could occur in the Application Extension, we
            // should check whether this combo follows directly after the 21.
            if (cur == 0x04 && prev == 0xF9) {
                getGifBytes(5);

                // Create Frame
                AnimatedGifFrame frame = new AnimatedGifFrame();

                byte buffer[] = new byte[5];
                // Copy to buffer
                System.arraycopy(mGifBuffer, 0, buffer, 0, 5);
                // Copy disposal Method
                frame.disposalMethod = (buffer[0] & 0x1c) >> 2;

                // We save the delays for easy access.
                frame.delay = ((buffer[1] & 0xFF) | (buffer[2] & 0xFF) << 8);
                // Gif stores the delay as 1/100 seconds, convert to milliseconds
                frame.delay = frame.delay * 10;

                byte board[] = new byte[8];
                board[0] = 0x21;
                board[1] = (byte) 0xF9;
                board[2] = 0x04;

                for (int i = 3, a = 0; a < 5; i++, a++) {
                    board[i] = buffer[a];
                }

                // Create Header
                frame.header = new byte[8];
                // Copy Header
                System.arraycopy(board, 0, frame.header, 0, 8);

                // Add Frame
                mFrames.add(frame);
                // Add delay to length
                mAnimationLength += frame.delay;
                break;
            }

            // Set current as previous
            prev = cur;
            // Read new byte
            getGifBytes(1);
            // Set cur
            cur = mGifBuffer[0] & 0xFF;
        }
    }
    
    private int convertToInt(byte firstByte, byte secondByte)
    {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(firstByte);
        bb.put(secondByte);

        return bb.getShort(0);        
    }

    private void readGifDescriptor() throws IOException {
        // Read 9 bytes
        getGifBytes(9);

        // Deep copy
        byte gifScreenTmp[] = new byte[mGifBuffer.length];
        System.arraycopy(mGifBuffer, 0, gifScreenTmp, 0, gifScreenTmp.length);

        byte aBuffer[] = new byte[9];
        System.arraycopy(mGifBuffer, 0, aBuffer, 0, 9);

        Rect rect = new Rect();
        rect.left = convertToInt(aBuffer[0], aBuffer[1]);
        rect.top = convertToInt(aBuffer[2], aBuffer[3]);
        rect.right = convertToInt(aBuffer[4], aBuffer[5]);
        rect.bottom = convertToInt(aBuffer[6], aBuffer[7]);


        // Get Last Object
        AnimatedGifFrame frame = mFrames.get(mFrames.size() - 1);
        frame.area = rect;

        if ((aBuffer[8] & 0x80) != 0x00) {
            mGifColorF = 1;
        } else {
            mGifColorF = 0;
        }

        byte gifCode = (byte) mGifColorC;
        byte gifSort = (byte) mGifSorted;

        if (mGifColorF == 1) {
            gifCode = (byte) (aBuffer[8] & 0x07);

            if ((aBuffer[8] & 0x20) != 0x00) {
                gifSort = 1;
            } else {
                gifSort = 0;
            }
        }

        int gifSize = (2 << gifCode);

        int blength = mGifScreen.length;
        byte bBuffer[] = new byte[blength];
        System.arraycopy(mGifScreen, 0, bBuffer, 0, blength);

        bBuffer[4] = (byte) (bBuffer[4] & 0x70);
        bBuffer[4] = (byte) (bBuffer[4] | 0x80);
        bBuffer[4] = (byte) (bBuffer[4] | gifCode);

        if (gifSort != 0x00) {
            bBuffer[4] |= 0x08;
        }

        // Create ByteArrayOutputStream for write the new "image"
        ByteArrayOutputStream gifString = new ByteArrayOutputStream();
        // Write Header (GIF89a)
        gifString.write(0x47);
        gifString.write(0x49);
        gifString.write(0x46);
        gifString.write(0x38);
        gifString.write(0x37);
        gifString.write(0x61);

        mGifScreen = new byte[blength];
        System.arraycopy(bBuffer, 0, mGifScreen, 0, blength);
        // Write Screen into gifString
        gifString.write(mGifScreen);

        if (mGifColorF == 1) {
            getGifBytes(3 * gifSize);
            gifString.write(mGifBuffer);
        } else {
            gifString.write(mGifGlobal);
        }

        // Add Graphic Control Extension Frame (for transparancy)
        gifString.write(frame.header);

        gifString.write(0x2c);

        int clength = gifScreenTmp.length;
        byte cBuffer[] = new byte[clength];
        System.arraycopy(gifScreenTmp, 0, cBuffer, 0, clength);

        // Mask
        cBuffer[8] &= 0x40;

        gifScreenTmp = new byte[clength];
        System.arraycopy(cBuffer, 0, gifScreenTmp, 0, clength);

        gifString.write(gifScreenTmp);
        getGifBytes(1);
        gifString.write(mGifBuffer);

        while (true) {
            getGifBytes(1);
            gifString.write(mGifBuffer);

            int dBuffer;
            dBuffer = mGifBuffer[0] & 0xFF;

            if (dBuffer != 0x00) {
                getGifBytes(dBuffer);
                gifString.write(mGifBuffer);
            } else {
                break;
            }

        }

        gifString.write(0x3b);

        // save the frame into the array of frames
        frame.data = gifString.toByteArray();
        // Create Image
        frame.gifFrame = BitmapFactory.decodeByteArray(frame.data, 0, frame.data.length);

        // Free Memory
        gifString.close();
    }

    private boolean getGifBytes(int numBytes) throws IOException {
        // Check Buffer Capacity
        if (mGifPointer.length > mDataPointer + numBytes) {
            // Create Output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Write on it
            baos.write(mGifPointer, mDataPointer, numBytes);
            // To buffer
            mGifBuffer = baos.toByteArray();

            // Increment Pointer
            mDataPointer += numBytes;

            // Free Memory
            baos.close();

            // Return ok
            return true;
        }

        return false;
    }

    private boolean skipGifBytes(int numBytes) {
        if (mGifPointer.length >= mDataPointer + numBytes) {
            // Increment
            mDataPointer += numBytes;

            // Return
            return true;
        }

        return false;
    }

    public static AnimationDrawable decodeStream(InputStream is) {
        try {
            // Get File Data
            byte data[] = GifDecoderFactory.getFileContents(is);
            // Decode Byte Array
            return GifDecoderFactory.decodeByteArray(data);
        } catch (IOException ioe) {
        }

        return null;
    }

    public static AnimationDrawable decodeByteArray(byte[] data) {
        if (data == null)
            return null;

        // Create Instance
        GifDecoderFactory decoder = new GifDecoderFactory(data);
        try {
            // Decode Gif
            decoder.decodeGif(data);
            // Create the full frames in memory to improve the painting of the image
            decoder.prepareFramesToDraw();
            // Return AnimationDrawable
            return decoder.createAnimationDrawable();
        } catch (IOException ioe) {
        }

        return null;
    }

    public static AnimationDrawable decodeResource(Resources res, int id) {
        return GifDecoderFactory.decodeStream(res.openRawResource(id));
    }

    public static byte[] getFileContents(InputStream is) throws IOException {
        // Read File
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[512];
        int nBytes;

        while ((nBytes = is.read(buffer)) != -1) {
            baos.write(buffer, 0, nBytes);
        }

        // Get File Data
        byte data[] = baos.toByteArray();
        baos.close();

        return data;
    }

    private static class AnimatedGifFrame {
        public byte   data[];
        public byte   header[];
        public double delay;
        public int    disposalMethod;
        public Rect   area;
        public Bitmap gifFrame;      // Frame from gif. It can be a full frame or a portion of an
                                      // image.
        public Bitmap fullFrame;     // Frame full painted
    }
}
