package com.feezrook.Paracart.Pets;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;

public class OwnAdvertActivity extends AppCompatActivity {

    private ImageButton close, download1, download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_advert);
        onClickListener();
        HideBar.hideSystemUI(getWindow().getDecorView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        HideBar.hideSystemUI(getWindow().getDecorView());
    }

    private void onClickListener() {
        close = findViewById(R.id.close);
        download1 = findViewById(R.id.download1);
        download = findViewById(R.id.download);

        close.setVisibility(View.INVISIBLE);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (close.getVisibility() == View.INVISIBLE) close.setVisibility(View.VISIBLE);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);

        close.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       close.setImageResource(R.drawable.closeadpress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                close.setImageResource(R.drawable.closead);
                                /*Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
                                startActivity(intent);*/
                                OwnAdvertActivity.this.finish();
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);
                    }
                }
        );
        download1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        download1.setImageResource(R.drawable.downpress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                download1.setImageResource(R.drawable.down);
                                Toast.makeText(OwnAdvertActivity.this, "Игра будет доступна в ближайшее время", Toast.LENGTH_LONG).show();

                            }

                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);
                    }
                }
        );

        download.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        download.setImageResource(R.drawable.downpress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                download.setImageResource(R.drawable.down);
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.feezrook.paracartclubs")));
                            }

                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);
                    }
                }
        );
    }

}
