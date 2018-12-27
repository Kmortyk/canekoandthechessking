package com.kmortyk.canekoandthechessking.resources;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.object.MapObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by user1 on 05.11.2018.
 */

public class ResourceManager {

    private static ResourceManager instance;

    private Resources resources = null;
    private String packageName;

    private ResourceManager() { }

    public static void Initialize(Context context) {
        if(instance == null) { instance = new ResourceManager(); }
        instance.resources   = context.getResources();
        instance.packageName = context.getPackageName();
    }

    public static ResourceManager getInstance() {
        if(instance.resources == null) {
            throw new NullPointerException("getInstance error: ResourceManager was not initialized");
        }

        return instance;
    }

    /**
     * @return string from strings.xml
     */
    public String getString(int id) { return resources.getString(id); }

    /**
     * All images are initially scaled by this value.
     */
    public float density() { return resources.getDisplayMetrics().density; }

    /* --- fonts ----------------------------------------------------------------------------------*/

    public Typeface loadTypeface(String name) { return Typeface.createFromAsset(resources.getAssets(), "fonts/" + name + ".otf"); }

    /* --- drawable -------------------------------------------------------------------------------*/

    public Bitmap loadDrawable(String name) {
        int id = getId(name, "drawable");

         if(id == 0)
             return BitmapFactory.decodeResource(resources, R.drawable.err);

        return BitmapFactory.decodeResource(resources, id);
    }

    public Bitmap loadDrawable(int idPath) { return BitmapFactory.decodeResource(resources, idPath); }

    public Bitmap loadDrawable(int idPath, double scale) {
        Bitmap btm = BitmapFactory.decodeResource(resources, idPath);
        return getResizedBitmap(btm, (int) (btm.getWidth() * scale), (int) (btm.getHeight() * scale));
    }

    /**
     * Scale bitmap
     * @return new scaled bitmap
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width  = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth  = (float) newWidth  / width;
        float scaleHeight = (float) newHeight / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    /**
     * Changes alpha channel for bitmap.
     * @param bitmap texture
     * @param opacity alpha value
     * @return same bitmap if mutable and create new if not
     */
    public static Bitmap adjustOpacity(Bitmap bitmap, int opacity)
    {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }

    /* --- maps -----------------------------------------------------------------------------------*/

    public ArrayList<MapObject> loadGameWorld(Bitmap map) {

        AssetManager assetManager = resources.getAssets();
        ArrayList<MapObject> world = new ArrayList<>();

        try {

            InputStream inputStream = assetManager.open("game_world");
            String s = convertStreamToString(inputStream);

            String[] lines = s.split("\n");

            for (String line : lines) {

                line = cleanString(line);
                String[] parts = line.split(" ");

                if (parts.length >= 5) {
                    MapObject o = new MapObject(parts[0],
                                                Float.valueOf(parts[1]) * density(),
                                                Float.valueOf(parts[2]) * density() - map.getHeight() / 2,
                                                parts[3].replace("_", " "),
                                                parts[4]);
                    o.centering();
                    world.add(o);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return world;
    }

    public ParsedMap loadMapFromAssets(String mapName) {

        AssetManager assetManager = resources.getAssets();
        ParsedMap parsedMap = new ParsedMap();

        try {

            InputStream inputStream = assetManager.open("maps/" + mapName);
            String s = convertStreamToString(inputStream);

            String[] lines = s.split("\n");

            for (int i = 0; i < lines.length; i++) {

                //remove non-printable unicode characters
                //used for each line because it also replaces \n
                String line = cleanString(lines[i]);

                if(line.equals("m:")) {

                    ArrayList<int[]> mapList = new ArrayList<>();

                    // while not next block (m:, e:, d:, etc...)
                    // and while i < length
                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) {

                        String[] strNums = line.split(" ");
                        ArrayList<Integer> numsList = new ArrayList<>();

                        for (String strNum : strNums) {
                            if (strNum.equals("")) continue;
                            numsList.add(Integer.valueOf(strNum));
                        }

                        int[] nums = new int[numsList.size()];
                        // unpacking not work with toArray(int[])
                        for (int j = 0; j < numsList.size(); j++) { nums[j] = numsList.get(j); }

                        mapList.add(nums);
                    }

                    parsedMap.map = mapList.toArray(new int[mapList.size()][]);
                    continue;

                }

                if(line.equals("e:")) {

                    ArrayList<String[]> enemiesList = new ArrayList<>();

                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) {
                            String[] parts = line.split(" ");
                            // length = 3: name, i, j
                            if (parts.length == 3) enemiesList.add(parts);
                    }

                    parsedMap.enemies = enemiesList.toArray(new String[enemiesList.size()][]);
                    continue;

                }

            }

            inputStream.close();

        } catch (IOException e) {
            Log.e("LoadMap", "No such map in assets: \"" + mapName + "\"");
            e.printStackTrace();
        }

        return parsedMap;
    }

    /* --- utils ----------------------------------------------------------------------------------*/

    /**
     * @param name resource name
     * @param defType drawable, raw, ...
     * @return ~R.id or 0 if error
     */
    private int getId(String name, String defType) { return resources.getIdentifier(name, defType, packageName); }

    /**
     * Removes non-printable characters (including '\n')
     */
    private String cleanString(String str) { return str.replaceAll("\\p{C}", ""); }

    /**
     * @return InputStream -> String =)
     */
    private String convertStreamToString(java.io.InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString("UTF-8");
    }

}
