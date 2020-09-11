package com.xiaobao.good;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.xiaobao.good.common.DateUtil;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.widget.CalendarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "Client_CA";

    @OnClick(R.id.iv_back)
    public void ivBack() {
        finish();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_name)
    EditText etName;

    @OnTextChanged(value = R.id.et_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterNameChanged(Editable s) {
        cacheClientBean.setClient_name(s.toString());
    }

    @BindView(R.id.et_phone)
    EditText etPhone;

    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        cacheClientBean.setClient_phone(s.toString());
    }

    @BindView(R.id.et_client_tag)
    EditText etTag;

    @OnTextChanged(value = R.id.et_client_tag, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterClientTagChanged(Editable s) {
        cacheClientBean.setClient_label(s.toString());
    }

    @BindView(R.id.cb_male)
    TextView cbMale;

    @OnClick({R.id.cb_male, R.id.tv_male})
    public void checkMale() {
        cacheClientBean.setClient_sex("男");
        cbMale.setBackgroundResource(R.mipmap.ic_selected);
        cbFemale.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_female)
    TextView cbFemale;

    @OnClick({R.id.tv_female, R.id.cb_female})
    public void checkFemale() {
        cacheClientBean.setClient_sex("女");
        cbMale.setBackgroundResource(R.mipmap.ic_unselected);
        cbFemale.setBackgroundResource(R.mipmap.ic_selected);
    }

    @BindView(R.id.et_idCard)
    EditText etIdCard;

    @OnTextChanged(value = R.id.et_idCard, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterIdCardChanged(Editable s) {
        cacheClientBean.setClient_idcard(s.toString());
    }

    @BindView(R.id.et_birth)
    TextView tvBirth;

    @OnClick(R.id.et_birth)
    public void selectBirthDay() {
        myCalendar();
    }

    @BindView(R.id.et_income)
    EditText etIncome;

    @OnTextChanged(value = R.id.et_income, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterIncomeChanged(Editable s) {
        if (StringUtils.isNumeric(s.toString())) {
            cacheClientBean.setClient_income(Integer.parseInt(s.toString()));
        }
    }

    @BindView(R.id.cb_married)
    TextView cbMarried;

    @OnClick({R.id.tv_married, R.id.cb_married})
    public void selectMarried() {
        cacheClientBean.setClient_marriage("已婚");
        cbMarried.setBackgroundResource(R.mipmap.ic_selected);
        cbUnmarried.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_unmarried)
    TextView cbUnmarried;

    @OnClick({R.id.tv_unmarried, R.id.cb_unmarried})
    public void selectUnmarried() {
        cacheClientBean.setClient_marriage("未婚");
        cbMarried.setBackgroundResource(R.mipmap.ic_unselected);
        cbUnmarried.setBackgroundResource(R.mipmap.ic_selected);
    }

    @BindView(R.id.cb_house_yes)
    TextView cbHouseYes;

    @OnClick({R.id.tv_house_yes, R.id.cb_house_yes})
    public void selectHouseYes() {
        cacheClientBean.setClient_house("是");
        cbHouseYes.setBackgroundResource(R.mipmap.ic_selected);
        cbHouseNo.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_house_no)
    TextView cbHouseNo;

    @OnClick({R.id.tv_house_no, R.id.cb_house_no})
    public void selectHouseNo() {
        cacheClientBean.setClient_house("否");
        cbHouseYes.setBackgroundResource(R.mipmap.ic_unselected);
        cbHouseNo.setBackgroundResource(R.mipmap.ic_selected);
    }

    @BindView(R.id.cb_car_yes)
    TextView cbCarYes;

    @OnClick({R.id.tv_car_yes, R.id.cb_car_yes})
    public void selectCarYes() {
        cacheClientBean.setClient_car("是");
        cbCarYes.setBackgroundResource(R.mipmap.ic_selected);
        cbCarNo.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_car_no)
    TextView cbCarNo;

    @OnClick({R.id.tv_car_no, R.id.cb_car_no})
    public void selectCarNo() {
        cacheClientBean.setClient_car("否");
        cbCarYes.setBackgroundResource(R.mipmap.ic_unselected);
        cbCarNo.setBackgroundResource(R.mipmap.ic_selected);
    }

    @BindView(R.id.et_address)
    EditText etAddress;

    @OnTextChanged(value = R.id.et_address, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterAddressChanged(Editable s) {
        cacheClientBean.setClient_address(s.toString());
    }

    @BindView(R.id.cb_level_p)
    TextView cbLevelP;

    @OnClick({R.id.tv_level_p, R.id.cb_level_p})
    public void selectLevelP() {
        cacheClientBean.setClient_type("P");
        cbLevelP.setBackgroundResource(R.mipmap.ic_selected);
        cbLevelA.setBackgroundResource(R.mipmap.ic_unselected);
        cbLevelC.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_level_a)
    TextView cbLevelA;

    @OnClick({R.id.tv_level_a, R.id.cb_level_a})
    public void selectLevelA() {
        cacheClientBean.setClient_type("A");
        cbLevelP.setBackgroundResource(R.mipmap.ic_unselected);
        cbLevelA.setBackgroundResource(R.mipmap.ic_selected);
        cbLevelC.setBackgroundResource(R.mipmap.ic_unselected);
    }

    @BindView(R.id.cb_level_c)
    TextView cbLevelC;

    @OnClick({R.id.tv_level_c, R.id.cb_level_c})
    public void selectLevelC() {
        cacheClientBean.setClient_type("C");
        cbLevelP.setBackgroundResource(R.mipmap.ic_unselected);
        cbLevelA.setBackgroundResource(R.mipmap.ic_unselected);
        cbLevelC.setBackgroundResource(R.mipmap.ic_selected);
    }

    @BindView(R.id.et_job)
    EditText etJob;

    @OnTextChanged(value = R.id.et_job, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterJobChanged(Editable s) {
        cacheClientBean.setClient_address(s.toString());
    }

    @OnClick(R.id.tv_cancel)
    public void cancel(TextView tvCancel) {
        finish();
    }

    @OnClick(R.id.tv_save)
    public void save(TextView tvSave) {
        try {
            Call<ResponseBody> call;
            LogUtil.d(TAG, "cacheClientBean : " + cacheClientBean);
            if (intentClientBean == null) {
                call = RetrofitUtils.getService().postClient(cacheClientBean);
            } else {
                call = RetrofitUtils.getService().putClient(cacheClientBean);
            }
            call.enqueue(
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                LogUtil.d(TAG, "response : " + response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.isSuccessful()) {
                                if (intentClientBean == null) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "添加客户成功",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "修改客户成功",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                                finish();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "接口请求失败",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            LogUtil.i(TAG, "fail re:" + t.getMessage());
                        }
                    });
        } catch (Exception e) {
            LogUtil.i(TAG, "save client info e:" + e);
        }
    }

    private Clients.DataBean.ClientsBean intentClientBean;
    private Clients.DataBean.ClientsBean cacheClientBean;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);

        gson = new Gson();
        cacheClientBean = new Clients.DataBean.ClientsBean();
        String strClientBean = getIntent().getStringExtra("ClientBean");
        if (StringUtils.isEmpty(strClientBean)) {
            tvTitle.setText("新增客户");
        } else {
            intentClientBean = gson.fromJson(strClientBean, Clients.DataBean.ClientsBean.class);
            tvTitle.setText("信息修改");
            initClientInfo();
        }
    }

    /**
     * 有客户信息传递过来时，显示已有的客户信息
     */
    private void initClientInfo() {
        etName.setText(intentClientBean.getClient_name());
        etPhone.setText(intentClientBean.getClient_phone());
        etTag.setText(intentClientBean.getClient_label());
        if ("男".equals(intentClientBean.getClient_sex())) {
            checkMale();
        } else if ("女".equals(intentClientBean.getClient_sex())) {
            checkFemale();
        }
        etIdCard.setText(intentClientBean.getClient_idcard());
        tvBirth.setText(DateUtil.DateToStringYMD(new Date(intentClientBean.getClient_birthday())));
        etIncome.setText(String.valueOf(intentClientBean.getClient_income()));
        if ("已婚".equals(intentClientBean.getClient_marriage())) {
            selectMarried();
        } else if ("未婚".equals(intentClientBean.getClient_marriage())) {
            selectUnmarried();
        }
        if ("是".equals(intentClientBean.getClient_house())) {
            selectHouseYes();
        } else if ("否".equals(intentClientBean.getClient_house())) {
            selectHouseNo();
        }
        if ("是".equals(intentClientBean.getClient_car())) {
            selectCarYes();
        } else if ("否".equals(intentClientBean.getClient_car())) {
            selectCarNo();
        }
        etAddress.setText(intentClientBean.getClient_address());
        if ("P".equals(intentClientBean.getClient_type())) {
            selectLevelP();
        } else if ("A".equals(intentClientBean.getClient_type())) {
            selectLevelA();
        } else if ("C".equals(intentClientBean.getClient_type())) {
            selectLevelC();
        }
        etJob.setText(intentClientBean.getClient_job());
    }

    private String year = "2000";
    private String month = "10";
    private String day = "1";
    private long birthdayLong;

    public void myCalendar() {
        // 初始化对话框             R.style.CalendarDialog 是自定义的弹框主题，在styles设置
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CalendarDialog);
        // 初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        // 绑定布局
        View customLayout =
                layoutInflater.inflate(
                        R.layout.view_slide_calendar, (ViewGroup) findViewById(R.id.customDialog));
        // 为对话框设置视图
        builder.setView(customLayout);

        // 加载年月日的三个 CalendarView 的 id
        CalendarView calendarView1 = (CalendarView) customLayout.findViewById(R.id.year);
        CalendarView calendarView2 = (CalendarView) customLayout.findViewById(R.id.month);
        CalendarView calendarView3 = (CalendarView) customLayout.findViewById(R.id.day);

        // 定义滚动选择器的数据项（年月日的）
        ArrayList<String> gradeYear = new ArrayList<>();
        ArrayList<String> gradeMonth = new ArrayList<>();
        ArrayList<String> gradeDay = new ArrayList<>();

        // 为数据项赋值
        int thisYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new java.util.Date()));
        for (int i = 1980; i <= thisYear; i++) // 从1980到今年
            gradeYear.add(i + "");
        for (int i = 1; i <= 12; i++) // 1月到12月
            gradeMonth.add(i + "");
        for (int i = 1; i <= 31; i++) // 1日到31日
            gradeDay.add(i + "");

        // 为滚动选择器设置数据
        calendarView1.setData(gradeYear);
        calendarView2.setData(gradeMonth);
        calendarView3.setData(gradeDay);

        // 滚动选择事件
        calendarView1.setOnSelectListener(data -> year = data);
        calendarView2.setOnSelectListener(data -> month = data);
        calendarView3.setOnSelectListener(data -> day = data);

        // 对话框的确定按钮
        builder.setPositiveButton(
                "确定",
                (dialog, which) -> {
                    tvBirth.setText(year + "-" + month + "-" + day);
                    birthDayToLong();
                });
        // 对话框的取消按钮
        builder.setNegativeButton("取消", null);
        // 显示对话框
        builder.show();
    }

    private void birthDayToLong() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        birthdayLong = calendar.getTimeInMillis();
        cacheClientBean.setClient_birthday(birthdayLong);
    }
}
