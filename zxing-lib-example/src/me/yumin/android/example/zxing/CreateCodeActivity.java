package me.yumin.android.example.zxing;

//package com.test.createcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 用于创建和扫描二维码
 */
public class CreateCodeActivity extends Activity {


    /**
     * 将要生成二维码的内容
     */
    private EditText codeEdit;

    /**
     * 生成二维码代码
     */
    private Button twoCodeBtn;
    /**
     * 用于展示生成二维码的imageView
     */
    private ImageView codeImg;

    /**
     * 生成一维码按钮
     */
    private Button oneCodeBtn;

    /**
     * 界面的初始化事件
     *
     * @param savedInstanceState Bundle对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        setListener();

			}

    /**
     * 用于初始化界面展示的view
     */
    private void initView() {
        codeEdit = (EditText) findViewById(R.id.code_edittext);
        twoCodeBtn = (Button) findViewById(R.id.code_btn);
        oneCodeBtn = (Button) findViewById(R.id.btn_code);
        codeImg = (ImageView) findViewById(R.id.code_img);
			}

    /**
     * 设置生成二维码和扫描二维码的事件
     */
    private void setListener() {
        twoCodeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = codeEdit.getText().toString().trim();
                Bitmap bmp = null;
                try {
                    if (str != null && !"".equals(str)) {
                        bmp = CreateTwoDCode(str);
											}
									} catch (WriterException e) {
                    e.printStackTrace();
									}
                if (bmp != null) {
                    codeImg.setImageBitmap(bmp);
									}

							}
					});


        oneCodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = codeEdit.getText().toString().trim();
                int size = str.length();
                for (int i = 0; i < size; i++) {
                    int c = str.charAt(i);
                    if ((19968 <= c && c < 40623)) {
                        Toast.makeText(CreateCodeActivity.this, "生成条形码的时刻不能是中文", Toast.LENGTH_SHORT).show();
                        return;
											}

									}
                Bitmap bmp = null;
                try {
                    if (str != null && !"".equals(str)) {
                        bmp = CreateOneDCode(str);
											}
									} catch (WriterException e) {
                    e.printStackTrace();
									}
                if (bmp != null) {
                    codeImg.setImageBitmap(bmp);
									}
							}
					});
			}

    /**
     * 将指定的内容生成成二维码
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateTwoDCode(String content) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
									}
							}
					}

        Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
			}

    /**
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
									}
							}
					}

        Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
			}

	}
