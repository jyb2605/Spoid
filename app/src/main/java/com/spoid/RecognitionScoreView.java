/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.spoid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.spoid.Classifier.Recognition;

import java.util.List;

public class RecognitionScoreView extends View {
    private static final float TEXT_SIZE_DIP = 24;
    private List<Recognition> results;
    private final float textSizePx;
    private final Paint fgPaint;
    private final Paint bgPaint;

    public RecognitionScoreView(final Context context, final AttributeSet set) {
        super(context, set);

//    textSizePx =
//        TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        textSizePx = 30;
//    Log.e("TextSize", String.valueOf(textSizePx));
        fgPaint = new Paint();
        fgPaint.setTextSize(textSizePx);

        bgPaint = new Paint();
//    bgPaint.setColor(0xcc4285f4);
        bgPaint.setColor(0xffffffff);

    }

    public void setResults(final List<Recognition> results) {
        this.results = results;
        postInvalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {


        if (results != null) {
            CameraConnectionFragment.items_list.clear();

            for (final Recognition recog : results) {
                if (recog.getConfidence() > 0.1) {
                    Log.e("recog", "ID : " + recog.getId() + ", TITLE : " + recog.getTitle() + ", recognition rate : " + recog.getConfidence());




                    switch (Integer.valueOf(recog.getId())) {
//                        case 543:
//                            CameraConnectionFragment.items_list.add(new Item("키보드", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.icon_laptop, R.drawable.logo));
//                            break;
                        case 552:
                            CameraConnectionFragment.items_list.add(new Item("노트북", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.icon_laptop, R.drawable.logo));
                            break;
//                        case 310:
                        case 330:
                            CameraConnectionFragment.items_list.add(new Item("꽃", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.group_8, R.drawable.logo));
                            break;
                        case 811:
                            CameraConnectionFragment.items_list.add(new Item("컵", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.icon_cup, R.drawable.logo));
                            break;
                        case 980:
                            CameraConnectionFragment.items_list.add(new Item("스마트폰", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.group_14, R.drawable.logo));
                            break;
                        case 309:
                            CameraConnectionFragment.items_list.add(new Item("의자", "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.icon_chair, R.drawable.logo));

                            break;

//                        default:
//                            CameraConnectionFragment.items_list.add(new Item(recog.getTitle(), "키보드+모니터", "망치", "노트북은 짱이다", R.drawable.logo, R.drawable.logo));
//                            break;


                    }

                }
            }
            CameraConnectionFragment.resultAdapter.notifyDataSetChanged();
        }

//
//        final int x = 50;
//        int y = (int) (fgPaint.getTextSize() * 1.5f);
//
////    canvas.drawPaint(bgPaint);
//
//
//        if (results != null) {
////      CameraConnectionFragment.parse_layout.removeAllViews();
//            for (final Recognition recog : results) {
//                Log.e("recog.getTitle()", recog.getTitle());
//                if (recog.getConfidence() > 0.2) {
////        canvas.drawText(recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
//                    canvas.drawText(recog.getTitle(), x, y, fgPaint);
//
////          @SuppressLint("DrawAllocation") LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(50, 50);
////          @SuppressLint("DrawAllocation") ImageView image = new ImageView(getContext());
////          image.setImageResource(R.drawable.parse);
////          image.setLayoutParams(parms);
////          // Adds the view to the layout
////          CameraConnectionFragment.parse_layout.addView(image);
//
//                    y += fgPaint.getTextSize() * 1.8f;
//                }
//            }
//        }
    }
}
