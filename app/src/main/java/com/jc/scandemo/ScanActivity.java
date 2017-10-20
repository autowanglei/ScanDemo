package com.jc.scandemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.jc.scandemo.utils.CheckPermissionUtils;
import com.jc.scandemo.utils.FinderPatternIndex;
import com.jc.scandemo.utils.QRDirection;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //初始化权限
        initPermission();
    }

    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }


    public void gotoScan( View view ) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ArrayList<String> xString = data.getStringArrayListExtra(CaptureActivity.X_LIST);
            ArrayList<String> yString = data.getStringArrayListExtra(CaptureActivity.Y_LIST);
            float[] x = QRDirection.floatArray2StringList(xString);
            float[] y = QRDirection.floatArray2StringList(yString);

            FinderPatternIndex finderPatternIndex = new FinderPatternIndex();
            finderPatternIndex.x = QRDirection.getIndex(x, x[0]);
            finderPatternIndex.y = QRDirection.getIndex(y, y[0]);

            int direction = QRDirection.getQRPicDirection(finderPatternIndex);
            int orientation = data.getIntExtra(CaptureActivity.ORIENTATION, 0);
            int orientationMax = 4;
            int tmpDirection;
            switch (orientation) {
                case 0:
                    tmpDirection = direction - 2;
                    direction = tmpDirection < orientationMax ? tmpDirection : tmpDirection + orientationMax;
                    break;
                case 90:
                    tmpDirection = direction - 1;
                    direction = tmpDirection < orientationMax ? tmpDirection : tmpDirection + orientationMax;
                    break;
                case 180:
                    break;
                case 270:
                    tmpDirection = direction + 1;
                    direction = tmpDirection < orientationMax ? tmpDirection : tmpDirection - orientationMax;
                    break;
                default:
                    break;
            }

            String directionStr = "未知";
            switch (direction) {
                case QRDirection.DIRECTION_UP:
                    directionStr = "上↑";
                    break;
                case QRDirection.DIRECTION_RIGHT:
                    directionStr = "右→";
                    break;
                case QRDirection.DIRECTION_DOWN:
                    directionStr = "下↓";
                    break;
                case QRDirection.DIRECTION_LEFT:
                    directionStr = "左←";
                    break;
                default:
                    break;
            }
            Toast.makeText(this, "direction: " + direction + " " + directionStr + " orientation:" + orientation,
                    Toast.LENGTH_LONG).show();
        }

    }
}
