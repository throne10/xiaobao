package com.xiaobao.good;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class ClientActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_client_tag)
    EditText etClientTag;

    @BindView(R.id.cb_male)
    CheckBox cbMale;

    @BindView(R.id.tv_male)
    TextView tv_male;

    @BindView(R.id.cb_female)
    CheckBox cbFemale;

    @BindView(R.id.tv_female)
    TextView tvFemale;

    @BindView(R.id.et_idCard)
    EditText etIdCard;

    @BindView(R.id.tv_birth)
    TextView tvBirth;

    @BindView(R.id.et_income)
    EditText etIncome;

    @BindView(R.id.tv_marriage)
    TextView tvMarriage;

    @BindView(R.id.cb_married)
    CheckBox cbMarried;

    @BindView(R.id.tv_married)
    TextView tvMarried;

    @BindView(R.id.cb_unmarried)
    CheckBox cbUnmarried;

    @BindView(R.id.tv_unmarried)
    TextView tvUnmarried;

    @BindView(R.id.tv_house)
    TextView tvHouse;

    @BindView(R.id.cb_house_yes)
    CheckBox cbHouseYes;

    @BindView(R.id.tv_house_yes)
    TextView tvHouseYes;

    @BindView(R.id.cb_house_no)
    CheckBox cbHouseNo;

    @BindView(R.id.tv_house_no)
    TextView tvHouseNo;

    @BindView(R.id.tv_car)
    TextView tvCar;

    @BindView(R.id.cb_car_yes)
    CheckBox cbCarYes;

    @BindView(R.id.tv_car_yes)
    TextView tvCarYes;

    @BindView(R.id.cb_car_no)
    CheckBox cbCarNo;

    @BindView(R.id.tv_car_no)
    TextView tvCarNo;

    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_level)
    TextView tvLevel;

    @BindView(R.id.cb_level_p)
    CheckBox cbLevelP;

    @BindView(R.id.tv_level_p)
    TextView tvLevelP;

    @BindView(R.id.cb_level_a)
    CheckBox cbLevelA;

    @BindView(R.id.tv_level_a)
    TextView tvLevelA;

    @BindView(R.id.cb_level_c)
    CheckBox cbLevelC;

    @BindView(R.id.tv_level_c)
    TextView tvLevelC;

    @BindView(R.id.et_job)
    EditText etJob;

    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    @BindView(R.id.tv_save)
    TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
    }
}
