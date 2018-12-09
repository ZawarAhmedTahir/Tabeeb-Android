package com.example.hp.assignment3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent=getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String age = intent.getStringExtra("age");
        String url = intent.getStringExtra("url");
        TextView name_=(TextView) findViewById(R.id.name);
        TextView email_=(TextView) findViewById(R.id.email);
        TextView age_=(TextView) findViewById(R.id.age);

        imageView = (ImageView) findViewById(R.id.profile_image);
        name_.setText(name);
        email_.append(email);
        age_.append(age);
        //Picasso.with(this).load(url).placeholder(R.drawable.placeholder).into(imageView);

        Picasso.with(this)
                .load(url)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded (Bitmap bitmap, Picasso.LoadedFrom from) {
            /* Save the bitmap or do something with it here */

                        bitmap=roundCornerImage(bitmap,59);
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                });

    }

    public Bitmap roundCornerImage(Bitmap raw, float round) {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, round, round, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(raw, rect, rect, paint);

        return result;
    }

}
