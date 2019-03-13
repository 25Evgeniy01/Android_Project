package com.feezrook.Paracart.Pets;
import android.content.Context;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

/**
 * Created by DIO on 26.10.2017.
 */

class GridAdapter extends BaseAdapter //class для прорисовки карточек
{
    private boolean variabParaWin = false;

    private Context mContext;
    private Integer mCols, mRows;
    private ArrayList<String> arrPict, arrClose; // array of picture
    private String PictureCollection, CloseColection; // firstname picture //firstnamen.jpg
    private Resources mRes; // Ресурсы приложени
    private int sizePicture;

    private enum Status {CELL_OPEN, CELL_CLOSE, CELL_DELETE};
    private ArrayList<Status> arrStatus; // состояние ячеек

    private ArrayList<Status> arrStatusOld;

    private int[] arrAnimation;
    private ArrayList<Animation> ANIM;
    public ArrayList<View> arrView;

    public static KonfettiView konfettiView;

    private int[] arrayOfRandomShow; //to show random cell
    private Handler handlerCheck = new Handler(); //for paused close
    private Runnable runnableCheck;





    private Animation.AnimationListener animationListenerCard = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public GridAdapter(Context context, int cols, int rows, int sizePicture) //
    {
        mContext = context;
        mCols = cols;
        mRows = rows;
        arrPict = new ArrayList<String>();
        arrClose = new ArrayList<String>();
        arrStatus = new ArrayList<Status>();
        arrayOfRandomShow = new int[(mCols * mRows) / 2];
        arrAnimation = new int[mCols*mRows];
        ANIM = new ArrayList(2);
        ANIM.add(AnimationUtils.loadAnimation(mContext, R.anim.card_in));
        ANIM.add(AnimationUtils.loadAnimation(mContext, R.anim.card_out));
        arrView = new ArrayList<>(mCols*mRows);
        this.sizePicture = sizePicture;

        konfettiView = null;

        // Пока определяем префикс так, позже он будет браться из настроек
        PictureCollection = "zoo";//"close1000_";//"c0_";//"nimber";
        CloseColection = "close";
        // Получаем все ресурсы приложения
        mRes = mContext.getResources();

        // Метод заполняющий массив vecPict
        makePictArray ();
        // Метод устанавливающий всем ячейкам статус CELL_CLOSE
        closeAllCells();

        getRandomNumber(4, 21);
    }

    private void closeAllCells () {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++)
            arrStatus.add(Status.CELL_CLOSE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView view; // выводиться у нас будет картинка
        if (position == 0) {
            arrView.clear(); //for animation
        }

        if (convertView == null)
            view = new ImageView(mContext);
        else
            view = (ImageView)convertView;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Bases.PREFERENCE_FOR_LEVEL, Context.MODE_PRIVATE);
        switch (arrStatus.get(position))
        {
            case CELL_OPEN:
                // Получаем идентификатор ресурса для картинки,
                // которая находится в векторе vecPict на позиции position
                Integer drawableId = mRes.getIdentifier(arrPict.get(position), "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);
                break;
            case CELL_CLOSE:
                int level = (sharedPreferences.getInt(mContext.getString(R.string.PREFERENCE_LEVEL), -1) + 2);
                //view.setImageResource(R.drawable.nimberclose);
                if (level%2==0 ){
                    Integer closeId = mRes.getIdentifier("close4x4", "drawable", mContext.getPackageName());
                    view.setImageResource(closeId);
                } else {
                    Integer closeId = mRes.getIdentifier("close2", "drawable", mContext.getPackageName());
                    view.setImageResource(closeId);
                }

                break;
            default:
                view.setImageResource(R.drawable.none);
        }

        view.setLayoutParams(new GridView.LayoutParams(sizePicture, sizePicture));
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        view.setPadding(3, 3, 3, 3);
        //view.startAnimation(ANIM.get(0));
        if (arrAnimation[position] == 1) {
            Animation animation = ANIM.get(0);
            arrAnimation[position] = 0;
            view.startAnimation(animation);
        } else if (arrAnimation[position]== 2) {
            Animation animation = ANIM.get(1);
            arrAnimation[position] = 0;
            view.startAnimation(animation);
        }
        arrView.add(view);
        return view;
    }


    private void makePictArray () {
        // очищаем вектор
        arrPict.clear();
        // добавляем
        int a1 = rnd(0, 6);
        int a2 = rnd(7, 12);
        int a3 = rnd(13, 19);

        if (mCols*mRows == 6) {

            arrPict.add(PictureCollection + Integer.toString(a1));
            arrPict.add(PictureCollection + Integer.toString(a1));

            arrPict.add(PictureCollection + Integer.toString(a2));
            arrPict.add(PictureCollection + Integer.toString(a2));

            arrPict.add(PictureCollection + Integer.toString(a3));
            arrPict.add(PictureCollection + Integer.toString(a3));

        } else {
            for (int i = 0; i < ((mCols * mRows) / 2); i++) {
                arrPict.add(PictureCollection + Integer.toString(i));
                arrPict.add(PictureCollection + Integer.toString(i));
            }
        }



        // перемешиваем
        Collections.shuffle(arrPict);

        if (mCols*mRows == 6) {

            arrayOfRandomShow[0]=arrPict.indexOf(PictureCollection + Integer.toString (a1));
            arrayOfRandomShow[1]=arrPict.indexOf(PictureCollection + Integer.toString (a2));
            arrayOfRandomShow[2]=arrPict.indexOf(PictureCollection + Integer.toString (a3));

        } else {
            for (int i = 0; i < ((mCols * mRows) / 2); i++) {
                arrayOfRandomShow[i]=arrPict.indexOf(PictureCollection + Integer.toString (i));
            }
        }


    }

    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @Override
    public int getCount() {
        return mCols*mRows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private int[] arr = {0, 0};
    public int[] checkOpenCells() {

        int first = arrStatus.indexOf(Status.CELL_OPEN);
        int second = arrStatus.lastIndexOf(Status.CELL_OPEN);

        if (first == second) {
            arr[0] = 0;
            arr[1] = 0;
            return arr;
        }

        if (arrPict.get(first).equals (arrPict.get(second)))
        {

            arr[0] = 1;
            arr[1] = 1;
            arrStatus.set(first, Status.CELL_DELETE);
            arrStatus.set(second, Status.CELL_DELETE);

            arrView.get(first).startAnimation(ANIM.get(1));
            arrView.get(second).startAnimation(ANIM.get(1));
            int size[][] = new int[2][2];
            arrView.get(first).getLocationOnScreen(size[0]);
            arrView.get(second).getLocationOnScreen(size[1]);
            konfetti(size[0]);
            konfetti(size[1]);
            variabParaWin = true;
            return arr;
            //notifyDataSetChanged();
        }
        else
        {
            arr[0] = 1;
            arr[1] = 0;
            arrStatus.set(first, Status.CELL_CLOSE);
            arrStatus.set(second, Status.CELL_CLOSE);
            arrView.get(first).startAnimation(ANIM.get(1));
            arrView.get(second).startAnimation(ANIM.get(1));
            arrAnimation[first] = 1;
            arrAnimation[second] = 1;
            variabParaWin = false;

            return arr;
        }

        //notifyDataSetChanged();

    }

    public boolean getVariabParaWin() {
        return variabParaWin;
    }

    public void checkDestroy() {  //checkOpenCells for konfetti
        int first = arrStatus.indexOf(Status.CELL_OPEN);
        int second = arrStatus.lastIndexOf(Status.CELL_OPEN);
        if (first == second)
            return;
        if (arrPict.get(first).equals (arrPict.get(second)))
        {
            arrStatus.set(first, Status.CELL_DELETE);
            arrStatus.set(second, Status.CELL_DELETE);

            arrView.get(first).startAnimation(ANIM.get(1));
            arrView.get(second).startAnimation(ANIM.get(1));
            int size[][] = new int[2][2];
            arrView.get(first).getLocationOnScreen(size[0]);
            arrView.get(second).getLocationOnScreen(size[1]);
            konfetti(size[0]);
            konfetti(size[1]);
        }

    }



    public boolean openCell(int position) {
        if (arrStatus.get(position) == Status.CELL_DELETE
                || arrStatus.get(position) == Status.CELL_OPEN)
            return false;

        if (arrStatus.get(position) != Status.CELL_DELETE) {
            arrStatus.set(position, Status.CELL_OPEN);
            //arrAnimation[position] = 1;

            /*
            runnableCheck = new Runnable() {

                public void run() {
                    checkDestroy();
                }
            };

            handlerCheck.postDelayed(runnableCheck, 500); //Close cell with pause
            */

            ANIM.get(1).setAnimationListener(animationListenerCard); //upgrade picture with notifyD.. when animation end
            //arrView.get(position).startAnimation(ANIM.get(1));
            notifyDataSetChanged();
        }

        //notifyDataSetChanged();
        return true;
    }

    public boolean checkGameOver() {
        if (arrStatus.indexOf(Status.CELL_CLOSE) < 0)
            return true;
        return false;
    }

    public int[] getArrayOfRandomShow() {
        return arrayOfRandomShow;
    }

    public void changeStatus(int[] arrToChange, int numberOfStatus, int countCellForShow) {
        Status status = Status.values()[numberOfStatus];
        if (countCellForShow <= arrToChange.length) {
            for (int i = 0; i < countCellForShow; i++) {
                arrStatus.set(arrToChange[i], status);
            }
        }
        notifyDataSetChanged();
    }

    private void getRandomNumber(int up, int count) {
        int i = 0;
        while (i < up) {
            arrClose.add(CloseColection + String.valueOf(i));
            i++;
        }
        Collections.shuffle(arrClose);
        Random random = new Random();
        while (i<count) {
            arrClose.add(CloseColection + String.valueOf(random.nextInt(up)));
            i++;
        }
    }



    private void konfetti(int[] size) {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE, Color.RED)
                .setDirection(0.0, 200.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new nl.dionsegijn.konfetti.models.Size(8, 5f))
                .setPosition(size[0], (float) size[0]+sizePicture, size[1]-sizePicture/2, (float) size[1]+sizePicture/2)
                .stream(200, 100L);

    }

}
