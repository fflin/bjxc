package com.zxwl.frame.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.rx.RxBus;

/**
 * 分屏样式选择对话框
 */
public class SplitScreenDialogActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivOne;
    private ImageView ivTwoA;
    private ImageView ivTwoB;
    private ImageView ivThreeA;
    private ImageView ivThreeB;
    private ImageView ivThreeC;
    private ImageView ivThreeD;
    private ImageView ivThreeE;
    private ImageView ivFourA;
    private ImageView ivFourB;
    private ImageView ivFourC;
    private ImageView ivFourD;
    private ImageView ivFourE;
    private ImageView ivFourF;
    private ImageView ivFiveA;
    private ImageView ivFiveB;
    private ImageView ivFiveC;
    private ImageView ivFiveD;
    private ImageView ivSixA;
    private ImageView ivSixB;
    private ImageView ivSixC;
    private ImageView ivSixD;
    private ImageView ivSixE;
    private ImageView ivSevenA;
    private ImageView ivSevenB;
    private ImageView ivSevenC;
    private ImageView ivSevenD;
    private ImageView ivSevenE;
    private ImageView ivEightA;
    private ImageView ivEightB;
    private ImageView ivEightC;
    private ImageView ivEightD;
    private ImageView ivNine;
    private ImageView ivTenA;
    private ImageView ivTenB;
    private ImageView ivTenC;
    private ImageView ivTenD;
    private ImageView ivTenE;
    private ImageView ivTenF;
    private ImageView ivThirteenA;
    private ImageView ivThirteenB;
    private ImageView ivThirteenC;
    private ImageView ivThirteenD;
    private ImageView ivThirteenE;
    private ImageView ivSixteen;
    private ImageView ivTwenty;
    private ImageView ivTwentyFour;

    public final static String CURRENT_INDEX = "current_index";

    public final static String CURRENT_CHLID_INDEX = "current_chlid_index";

    @Override
    protected void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwoA = (ImageView) findViewById(R.id.iv_two_a);
        ivTwoB = (ImageView) findViewById(R.id.iv_two_b);
        ivThreeA = (ImageView) findViewById(R.id.iv_three_a);
        ivThreeB = (ImageView) findViewById(R.id.iv_three_b);
        ivThreeC = (ImageView) findViewById(R.id.iv_three_c);
        ivThreeD = (ImageView) findViewById(R.id.iv_three_d);
        ivThreeE = (ImageView) findViewById(R.id.iv_three_e);
        ivFourA = (ImageView) findViewById(R.id.iv_four_a);
        ivFourB = (ImageView) findViewById(R.id.iv_four_b);
        ivFourC = (ImageView) findViewById(R.id.iv_four_c);
        ivFourD = (ImageView) findViewById(R.id.iv_four_d);
        ivFourE = (ImageView) findViewById(R.id.iv_four_e);
        ivFourF = (ImageView) findViewById(R.id.iv_four_f);
        ivFiveA = (ImageView) findViewById(R.id.iv_five_a);
        ivFiveB = (ImageView) findViewById(R.id.iv_five_b);
        ivFiveC = (ImageView) findViewById(R.id.iv_five_c);
        ivFiveD = (ImageView) findViewById(R.id.iv_five_d);
        ivSixA = (ImageView) findViewById(R.id.iv_six_a);
        ivSixB = (ImageView) findViewById(R.id.iv_six_b);
        ivSixC = (ImageView) findViewById(R.id.iv_six_c);
        ivSixD = (ImageView) findViewById(R.id.iv_six_d);
        ivSixE = (ImageView) findViewById(R.id.iv_six_e);
        ivSevenA = (ImageView) findViewById(R.id.iv_seven_a);
        ivSevenB = (ImageView) findViewById(R.id.iv_seven_b);
        ivSevenC = (ImageView) findViewById(R.id.iv_seven_c);
        ivSevenD = (ImageView) findViewById(R.id.iv_seven_d);
        ivSevenE = (ImageView) findViewById(R.id.iv_seven_e);
        ivEightA = (ImageView) findViewById(R.id.iv_eight_a);
        ivEightB = (ImageView) findViewById(R.id.iv_eight_b);
        ivEightC = (ImageView) findViewById(R.id.iv_eight_c);
        ivEightD = (ImageView) findViewById(R.id.iv_eight_d);
        ivNine = (ImageView) findViewById(R.id.iv_nine);
        ivTenA = (ImageView) findViewById(R.id.iv_ten_a);
        ivTenB = (ImageView) findViewById(R.id.iv_ten_b);
        ivTenC = (ImageView) findViewById(R.id.iv_ten_c);
        ivTenD = (ImageView) findViewById(R.id.iv_ten_d);
        ivTenE = (ImageView) findViewById(R.id.iv_ten_e);
        ivTenF = (ImageView) findViewById(R.id.iv_ten_f);
        ivThirteenA = (ImageView) findViewById(R.id.iv_thirteen_a);
        ivThirteenB = (ImageView) findViewById(R.id.iv_thirteen_b);
        ivThirteenC = (ImageView) findViewById(R.id.iv_thirteen_c);
        ivThirteenD = (ImageView) findViewById(R.id.iv_thirteen_d);
        ivThirteenE = (ImageView) findViewById(R.id.iv_thirteen_e);
        ivSixteen = (ImageView) findViewById(R.id.iv_sixteen);
        ivTwenty = (ImageView) findViewById(R.id.iv_twenty);
        ivTwentyFour = (ImageView) findViewById(R.id.iv_twenty_four);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        tvTitle.setOnClickListener(this);
        ivOne.setOnClickListener(this);
        ivTwoA.setOnClickListener(this);
        ivTwoB.setOnClickListener(this);
        ivThreeA.setOnClickListener(this);
        ivThreeB.setOnClickListener(this);
        ivThreeC.setOnClickListener(this);
        ivThreeD.setOnClickListener(this);
        ivThreeE.setOnClickListener(this);
        ivFourA.setOnClickListener(this);
        ivFourB.setOnClickListener(this);
        ivFourC.setOnClickListener(this);
        ivFourD.setOnClickListener(this);
        ivFourE.setOnClickListener(this);
        ivFourF.setOnClickListener(this);
        ivFiveA.setOnClickListener(this);
        ivFiveB.setOnClickListener(this);
        ivFiveC.setOnClickListener(this);
        ivFiveD.setOnClickListener(this);
        ivSixA.setOnClickListener(this);
        ivSixB.setOnClickListener(this);
        ivSixC.setOnClickListener(this);
        ivSixD.setOnClickListener(this);
        ivSixE.setOnClickListener(this);
        ivSevenA.setOnClickListener(this);
        ivSevenB.setOnClickListener(this);
        ivSevenC.setOnClickListener(this);
        ivSevenD.setOnClickListener(this);
        ivSevenE.setOnClickListener(this);
        ivEightA.setOnClickListener(this);
        ivEightB.setOnClickListener(this);
        ivEightC.setOnClickListener(this);
        ivEightD.setOnClickListener(this);
        ivNine.setOnClickListener(this);
        ivTenA.setOnClickListener(this);
        ivTenB.setOnClickListener(this);
        ivTenC.setOnClickListener(this);
        ivTenD.setOnClickListener(this);
        ivTenE.setOnClickListener(this);
        ivTenF.setOnClickListener(this);
        ivThirteenA.setOnClickListener(this);
        ivThirteenB.setOnClickListener(this);
        ivThirteenC.setOnClickListener(this);
        ivThirteenD.setOnClickListener(this);
        ivThirteenE.setOnClickListener(this);
        ivSixteen.setOnClickListener(this);
        ivTwenty.setOnClickListener(this);
        ivTwentyFour.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_split_screen_dialog;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            //关闭
            case R.id.tv_title:
                break;

            //1
            case R.id.iv_one:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 1);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            //2
            case R.id.iv_two_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 2);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //2
            case R.id.iv_two_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 2);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            //3-a
            case R.id.iv_three_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 3);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //3-b
            case R.id.iv_three_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 3);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //3-c
            case R.id.iv_three_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 3);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //3-d
            case R.id.iv_three_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 3);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            //3-e
            case R.id.iv_three_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 3);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;


            //4-a
            case R.id.iv_four_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 4);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //4-b
            case R.id.iv_four_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 4);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //4-c
            case R.id.iv_four_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 4);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //4-d
            case R.id.iv_four_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 4);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            //4-e
            case R.id.iv_four_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 4);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;
//            //4-f
//            case R.id.iv_four_f:
//                intent = new Intent();
//                intent.putExtra(CURRENT_INDEX, 4);
//                intent.putExtra(CURRENT_CHLID_INDEX, 5);
//                break;

            //5-a
            case R.id.iv_five_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 5);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //5-b
            case R.id.iv_five_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 5);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //5-c
            case R.id.iv_five_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 5);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //5-d
            case R.id.iv_five_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 5);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;

            //6
            case R.id.iv_six_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 6);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //6
            case R.id.iv_six_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 6);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //6
            case R.id.iv_six_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 6);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //6
            case R.id.iv_six_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 6);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            //6
            case R.id.iv_six_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 6);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;

            //7
            case R.id.iv_seven_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 7);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //7
            case R.id.iv_seven_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 7);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //7
            case R.id.iv_seven_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 7);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //7
            case R.id.iv_seven_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 7);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            //7
            case R.id.iv_seven_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 7);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;

            //8
            case R.id.iv_eight_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 8);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //8
            case R.id.iv_eight_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 8);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //8
            case R.id.iv_eight_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 8);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //8
            case R.id.iv_eight_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 8);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;

            //9
            case R.id.iv_nine:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 9);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            //10
            case R.id.iv_ten_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            //10
            case R.id.iv_ten_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            //10
            case R.id.iv_ten_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            //10
            case R.id.iv_ten_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            //10
            case R.id.iv_ten_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;
            //10
            case R.id.iv_ten_f:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 10);
                intent.putExtra(CURRENT_CHLID_INDEX, 5);
                break;

            //13
            case R.id.iv_thirteen_a:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 13);
                intent.putExtra(CURRENT_CHLID_INDEX, 0);
                break;
            case R.id.iv_thirteen_b:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 13);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;
            case R.id.iv_thirteen_c:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 13);
                intent.putExtra(CURRENT_CHLID_INDEX, 2);
                break;
            case R.id.iv_thirteen_d:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 13);
                intent.putExtra(CURRENT_CHLID_INDEX, 3);
                break;
            case R.id.iv_thirteen_e:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 13);
                intent.putExtra(CURRENT_CHLID_INDEX, 4);
                break;

            //16
            case R.id.iv_sixteen:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 16);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            //20
            case R.id.iv_twenty:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 20);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            //24
            case R.id.iv_twenty_four:
                intent = new Intent();
                intent.putExtra(CURRENT_INDEX, 24);
                intent.putExtra(CURRENT_CHLID_INDEX, 1);
                break;

            default:
                break;
        }

        if (null != intent) {
            RxBus.getInstance().post(intent);
        }
        finish();
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SplitScreenDialogActivity.class));
    }
}
