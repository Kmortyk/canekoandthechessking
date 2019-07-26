package com.kmortyk.canekoandthechessking.resources;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.object.MapObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
        instance.resources    = context.getResources();
        instance.packageName  = context.getPackageName();

        standardTypeface = instance.loadTypeface("sofiapro_light");
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
    public String getString(String name) {
        int id = getResourceId(name, "string");
        if(id == 0) return "???";
        return getString(id);
    }

    public String getString(int id) { return resources.getString(id); }

    /**
     * All images are initially scaled by this value.
     */
    public float density() { return resources.getDisplayMetrics().density; }

    public static float pxFromDp(final float dp) {
        Resources res = getInstance().resources; // simplify using
        return dp * ((float) res.getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float dpFromPx(final float px) {
        Resources res = getInstance().resources;
        return px / ((float) res.getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /* --- fonts ----------------------------------------------------------------------------------*/

    public static Typeface standardTypeface;

    public Typeface loadTypeface(String name) { return Typeface.createFromAsset(resources.getAssets(), "fonts/" + name + ".otf"); }


    /* --- drawable -------------------------------------------------------------------------------*/

    public Bitmap loadDrawable(String name) {
        int id = getResourceId(name, "drawable");

         if(id == 0)
             return BitmapFactory.decodeResource(resources, R.drawable.err);

        return BitmapFactory.decodeResource(resources, id);
    }

    public static Bitmap emptyBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

    /**
     * Returns the bitmap with the resource with the specified id.
     * @param idPath id of resource, exm: R.drawable.grass
     *
     * @see GameResources#getDrawable(int id, float scaleFactor)
     */
    public Bitmap loadDrawable(int idPath) { return loadDrawable(idPath, 1); }

    public Bitmap loadDrawable(int idPath, double scale) {
        Drawable drawable = ResourcesCompat.getDrawable(resources, idPath, null);
        if(drawable == null) return emptyBitmap;
        return drawableToBitmap(drawable, (int) (drawable.getIntrinsicWidth() * scale), (int) (drawable.getIntrinsicHeight() * scale));
    }

    public Bitmap loadDrawable(int idPath, float width, float height) {
        Drawable drawable = ResourcesCompat.getDrawable(resources, idPath, null);
        if(drawable == null) return emptyBitmap;
        return drawableToBitmap(drawable, width, height);
    }

    /**
     * Convert drawable to bitmap.
     */
    private Bitmap drawableToBitmap(Drawable drawable, float width, float height) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
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

    public boolean mapExists(String mapName) {
        try {
            return Arrays.asList(resources.getAssets().list("maps")).contains(mapName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<MapObject> loadGameWorld() {

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
                                                Float.valueOf(parts[2]) * density(),
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
        parsedMap.mapName = mapName;

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

                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) { parseLine(line, enemiesList); }

                    parsedMap.enemies = enemiesList.toArray(new String[enemiesList.size()][]);
                    continue;

                }

                if(line.equals("d:")) {

                    ArrayList<String[]> decorationsList = new ArrayList<>();

                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) { parseLine(line, decorationsList); }

                    parsedMap.decorations = decorationsList.toArray(new String[decorationsList.size()][]);
                    continue;

                }

                if(line.equals("i:")) {

                    ArrayList<String[]> itemsList = new ArrayList<>();

                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) { parseLine(line, itemsList); }

                    parsedMap.items = itemsList.toArray(new String[itemsList.size()][]);
                    continue;

                }

                if(line.equals("param:")) {
                    while( ++i < lines.length && !(line = cleanString(lines[i])).equals("") ) { parseParam(line, parsedMap); }
                    // continue;
                }

            }

            inputStream.close();

        } catch (IOException e) {
            Log.e("LoadMap", "No such map in assets: \"" + mapName + "\"");
            e.printStackTrace();
        }

        return parsedMap;
    }

    private void parseParam(String line, ParsedMap map) {
        if(line.charAt(0) == '#') return;
        String[] parts = line.split(" ");
        switch (parts[0]) {
            case "hero":
                if(parts.length == 3) {
                    map.heroI = Integer.valueOf(parts[1]);
                    map.heroJ = Integer.valueOf(parts[2]);
                }
                break;
            case "back": if(parts.length == 2) { map.backType = parts[1]; }
                break;
            case "next": if(parts.length == 2) { map.nextMap = parts[1]; }
                break;
            default: Log.d("ResourceManager",
                          "loadMapFromAssets: no such param in file \"" + map.mapName + "\" (param:" + parts[0] + ")");
        }
    }

    private void parseLine(String line, ArrayList<String[]> to) {
        if(line.charAt(0) == '#') return;
        String[] parts = line.split(" ");
        // length = 3: name, i, j
        if (parts.length == 3) to.add(parts);
    }

    /* --- utils ----------------------------------------------------------------------------------*/

    /**
     * @param name resource name
     * @return ~R.id or 0 if error
     */
    public int getResourceId(String name, String defType) { return resources.getIdentifier(name, defType, packageName); }

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
