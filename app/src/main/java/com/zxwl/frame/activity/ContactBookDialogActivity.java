package com.zxwl.frame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zxwl.frame.R;
import com.zxwl.frame.adapter.ConfirmAdapter;
import com.zxwl.frame.adapter.EmployeeAdapter;
import com.zxwl.frame.adapter.SimpleTreeAdapter;
import com.zxwl.frame.bean.ClickEvent;
import com.zxwl.frame.bean.ConfirmEvent;
import com.zxwl.frame.bean.Department;
import com.zxwl.frame.bean.Employee;
import com.zxwl.frame.bean.SelectEvent;
import com.zxwl.frame.net.Urls;
import com.zxwl.frame.views.treeListView.bean.Node;
import com.zxwl.frame.views.treeListView.bean.TreeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class ContactBookDialogActivity extends Activity implements View.OnClickListener {
    private List<Department> mDatas3 = new ArrayList<Department>();//组织部门总数量
    //private List<Department> mDatas4 = new ArrayList<Department>();
    private List<Employee> allEmployee = new ArrayList<>();//人员总数量
    // private List<Employee> itemDepartment = new ArrayList<>();
    private List<Employee> selectedDepartment = new ArrayList<>();//选中的部门下面人员的数据，即左侧列表数据
    private ListView mTree;//组织部门列表
    private SimpleTreeAdapter<Department> mAdapter;//组织部门列表适配器
    private EmployeeAdapter employeeAdapter;//人员列表适配器
    private ListView lv_employee;//人员列表

    private CheckBox cb_checkAll;//全选按钮
    private TextView tv_checkedNum;//选中数量
    private int checkedNum = 0;
    private TextView tv_allNum;//所有组织部门的数量的文本
    private int allNum;//所有组织部门的数量
    private Department department;
    private EventBus eventBus;
    private boolean isChange = false;
    private int size;//接收到eventbus发送的checkbox选中数量
    private TextView tvNum;//右侧列表上方选中数量
    private Button btn_clear;//清空
    private List<Employee> confirmEmployee = new ArrayList<>();//点击通讯录中确认按钮后的数据，即传递给最终确认对话框中列表的数据
    private Button btn_confirm;
    private Button btn_cancel;
    private ImageView iv_close;//关闭对话框按钮
    private ConfirmAdapter confirmAdapter;//最终确认对话框中列表的适配器
    private List<Employee> dialogListEmployee;//最终确认对话框中数据
    private List<Node> nodes;//将后台返回数据转换后的节点数据
    private TextView tv_departmentFailed;//右侧列表请求失败或者没有数据时显示显示
    private TextView tv_employeeFailed;//左侧列表请求失败或者没有数据时显示
    private AVLoadingIndicatorView departmentLoadingView;//右侧列表请求对话框
    private AVLoadingIndicatorView employeeLoadingView;//左侧列表请求对话框

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ContactBookDialogActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactbook_dialog);
        eventBus = EventBus.getDefault();
        eventBus.register(this);

        mTree = (ListView) findViewById(R.id.id_tree);
        lv_employee = (ListView) findViewById(R.id.lv_employee);
        cb_checkAll = (CheckBox) findViewById(R.id.cb_checkAll);
        tv_checkedNum = (TextView) findViewById(R.id.tv_checkedNum);
        tv_allNum = (TextView) findViewById(R.id.tv_allNum);
        tvNum = (TextView) findViewById(R.id.tv_num);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_employeeFailed = (TextView) findViewById(R.id.tv_employeeFailed);
        tv_departmentFailed = (TextView) findViewById(R.id.tv_departmentFailed);
        departmentLoadingView = (AVLoadingIndicatorView) findViewById(R.id.aviDepartmentLoading);
        employeeLoadingView = (AVLoadingIndicatorView) findViewById(R.id.aviEmployeeLoading);
        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        tv_departmentFailed.setOnClickListener(this);
        tv_employeeFailed.setOnClickListener(this);
        //获取右侧组织部门数据
        initDepartmentDatas();
        //获取人员数据
        initEmployeeDatas();

        tvNum.setText("(" + 0 + ")");

        //全选按钮
        cb_checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //全选操作
                    isChange = false;
                    selectedDepartment.clear();
                    selectedDepartment.addAll(allEmployee);
                    setAdapter(selectedDepartment);
                    tvNum.setText("(" + allEmployee.size() + ")");
                    tv_employeeFailed.setVisibility(View.GONE);

                } else {
                    //取消全选操作
                    if (!isChange) {
                        selectedDepartment.clear();
                        employeeAdapter.notifyDataSetChanged();
                        tvNum.setText("(" + 0 + ")");
                        tv_employeeFailed.setText(getResources().getString(R.string.contact_book_nodata));
                        tv_employeeFailed.setVisibility(View.VISIBLE);
                    }
                }
                try {
                    //控制部门列表上分选中的数量
                    HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
                    int count = 0;
//                    if (isChecked) {
//                        isChange = false;
//                    }
                    for (int i = 0, p = mDatas3.size(); i < p; i++) {
                        if (isChecked) {
                            map.put(i, true);
                            count++;
                        } else {
                            if (!isChange) {
                                map.put(i, false);
                                count = 0;
                            } else {
                                map = mAdapter.getMap();
                                count = map.size();
                            }
                        }
                    }
                    setText(count, allNum);
                    mAdapter.setMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    //获取所有人员
    private void initEmployeeDatas() {
        employeeLoadingView.setVisibility(View.VISIBLE);
        employeeLoadingView.show();
        tv_employeeFailed.setVisibility(View.GONE);
        OkHttpUtils.get()
                .url(Urls.BASE_URL + "employeeAction_queryList1.action")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tv_employeeFailed.setVisibility(View.VISIBLE);
                        tv_employeeFailed.setText(getResources().getString(R.string.contact_book_failed));
                        employeeLoadingView.hide();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            employeeLoadingView.hide();
                            tv_employeeFailed.setVisibility(View.GONE);
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("dataList");

                            if (array.length() != 0) {
                                tv_employeeFailed.setVisibility(View.GONE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    Employee employee = new Employee(object1.getString("id"), object1.getString("name"));
                                    employee.setId(object1.getString("id"));
                                    employee.setUserName(object1.getString("userName"));
                                    employee.setName(object1.getString("name"));
                                    employee.setOrgNo(object1.getString("orgNo"));
                                    employee.setTerminalId(object1.getString("terminalId"));
                                    employee.setOrgName(object1.getString("orgName"));
                                    employee.setTypeName(object1.getString("typeName"));
                                    allEmployee.add(employee);
                                }
                            } else {
                                tv_employeeFailed.setVisibility(View.VISIBLE);
                                tv_employeeFailed.setText(R.string.contact_book_nodata);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取组织
    private void initDepartmentDatas() {
        departmentLoadingView.setVisibility(View.VISIBLE);
        departmentLoadingView.show();
        tv_departmentFailed.setVisibility(View.GONE);

        OkHttpUtils.get()
                .url(Urls.BASE_URL + Urls.QUERY_ALL_DEPARTMENT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tv_departmentFailed.setVisibility(View.VISIBLE);
                        tv_departmentFailed.setText(getResources().getString(R.string.contact_book_failed));
                        departmentLoadingView.hide();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            departmentLoadingView.hide();
                            JSONArray array = new JSONArray(response.toString());
                            if (array.length() != 0) {
                                tv_departmentFailed.setVisibility(View.GONE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject item = array.getJSONObject(i);
                                    Department department = new Department(item.getString("id"), item.getString("parentId"), item.getString("departmentName"));
                                    department.setId(item.getString("id"));
                                    department.setpId(item.getString("parentId"));
                                    department.setDepartmentName(item.getString("departmentName"));
                                    department.setIsParent(item.getString("isParent"));
                                    mDatas3.add(department);

                                }
                                mAdapter = new SimpleTreeAdapter(mTree, ContactBookDialogActivity.this, mDatas3, 10, eventBus);
                                mTree.setAdapter(mAdapter);
                                //对后台返回数据进行排序和转换
                                nodes = TreeHelper.getSortedNodes(mDatas3, -1);
                                allNum = mDatas3.size();
                                setText(0, allNum);
                            } else {
                                tv_departmentFailed.setVisibility(View.VISIBLE);
                                tv_departmentFailed.setText(R.string.contact_book_nodata);
                            }
                            //setTreeClick();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //接收条目中checkbox事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SelectEvent event) {
        size = event.getSize();
        boolean select = event.select;
        int position = event.position;
//        Log.i("TAG", "size====：" + size);
        if (size < mDatas3.size()) {
            isChange = true;
            cb_checkAll.setChecked(false);
        } else {
            cb_checkAll.setChecked(true);
            isChange = false;
        }
        //点击
        if (select) {
            String orgNo = nodes.get(position).getId();
            List<Employee> tempCheck = new ArrayList<>();//临时存储
            tempCheck.clear();//每次点击先重置，用来确定该单位下有没有数据
            for (int i = 0; i < allEmployee.size(); i++) {
                if (allEmployee.get(i).getOrgNo().equals(orgNo)) {
                    selectedDepartment.add(allEmployee.get(i));
                    tempCheck.add(allEmployee.get(i));
                }
            }

            if (tempCheck.size() == 0) {
                Toast.makeText(this, "此单位下没有数据！", Toast.LENGTH_SHORT).show();
            }

            if (selectedDepartment.size() == 0) {
                tv_employeeFailed.setVisibility(View.VISIBLE);
                tv_employeeFailed.setText(R.string.contact_book_nodata);
            } else {
                tv_employeeFailed.setVisibility(View.GONE);
            }

            setAdapter(selectedDepartment);
//            employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, selectedDepartment, eventBus);
//            lv_employee.setAdapter(employeeAdapter);
        } else {//取消
            String orgNo = nodes.get(position).getId();
            List<Employee> tempUnCheck = new ArrayList<>();//临时存储
            for (int i = 0; i < allEmployee.size(); i++) {
                if (allEmployee.get(i).getOrgNo().equals(orgNo)) {
                    tempUnCheck.add(allEmployee.get(i));
                }
            }
            if (tempUnCheck.size() == 0) {
                Toast.makeText(this, "此单位下没有数据！", Toast.LENGTH_SHORT).show();
            }
            selectedDepartment.removeAll(tempUnCheck);//删除该单位下所有数据
            tempUnCheck.clear();

            if (selectedDepartment.size() == 0) {
                tv_employeeFailed.setVisibility(View.VISIBLE);
                tv_employeeFailed.setText(R.string.contact_book_nodata);
            } else {
                tv_employeeFailed.setVisibility(View.GONE);
            }
        }
        setAdapter(selectedDepartment);
        setText(size, allNum);
        tvNum.setText("(" + selectedDepartment.size() + ")");
    }

    private void setAdapter(List<Employee> list) {
        employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this, list, eventBus);
        lv_employee.setAdapter(employeeAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    //设置选中数量
    private void setText(int checkedNum, int allNum) {
        tv_checkedNum.setText(checkedNum + "");
        tv_allNum.setText(allNum + "");
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //接收employeeAdapter中点击事件
    @Subscribe
    public void onClickEvent(ClickEvent event) {
        boolean isClicked = event.isClicked;
        int p = event.p;
        Employee employee = selectedDepartment.get(p);
        if (isClicked) {
            //选中
            if (!confirmEmployee.contains(employee)) {
                confirmEmployee.add(employee);
            }
        } else {
            //取消选中
            if (confirmEmployee.contains(employee)) {
                confirmEmployee.remove(employee);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel://取消
                finish();
                break;

            case R.id.btn_confirm: //确认
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("参会列表")
                        .customView(initDialogContent(), false)
                        .positiveText("确认")
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Toast.makeText(ContactBookDialogActivity.this, "11", Toast.LENGTH_SHORT).show();
                                eventBus.post(new ConfirmEvent(confirmEmployee, allEmployee.size()));
                                finish();
                            }
                        })
                        .build();
                dialog.show();
                break;

            case R.id.btn_clear://清空
                confirmEmployee.clear();
                selectedDepartment.clear();
                for (int i = 0, p = mDatas3.size(); i < p; i++) {
                    mAdapter.getMap().put(i, false);
                }
                mAdapter.notifyDataSetChanged();
                cb_checkAll.setChecked(false);
                setText(0, allNum);
                tvNum.setText("(" + 0 + ")");
                tv_employeeFailed.setVisibility(View.VISIBLE);
                tv_employeeFailed.setText(R.string.contact_book_nodata);
                break;

            case R.id.iv_close://关闭按钮
                finish();
                break;

            case R.id.tv_departmentFailed:
                if (getResources().getString(R.string.contact_book_failed).equals(tv_departmentFailed.getText().toString())) {
                    initDepartmentDatas();
                }
                break;

            case R.id.tv_employeeFailed:
                if (getResources().getString(R.string.contact_book_failed).equals(tv_employeeFailed.getText().toString())) {
                    initEmployeeDatas();
                }
        }
    }

    //确认对话框，滑动删除
    private View initDialogContent() {
        dialogListEmployee = new ArrayList<>();
        dialogListEmployee.clear();
        dialogListEmployee.addAll(confirmEmployee);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.contactbook_dialog, null);
        SwipeMenuListView lv_confirm = (SwipeMenuListView) dialogView.findViewById(R.id.lv_confirm);
        confirmAdapter = new ConfirmAdapter(dialogListEmployee, this);
        lv_confirm.setAdapter(confirmAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        lv_confirm.setMenuCreator(creator);
        lv_confirm.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        if (dialogListEmployee.contains(dialogListEmployee.get(position))) {
                            dialogListEmployee.remove(position);
                            confirmAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        return dialogView;
    }

    private void setTreeClick() {
        //部门列表条目点击事件
//        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//            @Override
//            public void onClick(Node node, int position) {
//                    mDatas5.clear();
//                    String orgNo = mDatas3.get(position).getId();
//                    Log.i("TAG","name==="+mDatas3.get(position).getDepartmentName()+","+"orgNo===="+orgNo+","+position);
//                    for (int i=0;i<mDatas4.size();i++){
//                        if (mDatas4.get(i).getOrgNo().equals(orgNo)){
//                            mDatas5.add(mDatas4.get(i));
//                        }
//                    }
//                    if (mDatas5.size()==0){
//                        tv_noData.setVisibility(View.VISIBLE);
//                    }else{
//                        tv_noData.setVisibility(View.GONE);
//                    }
//                    employeeAdapter = new EmployeeAdapter(ContactBookDialogActivity.this,mDatas5);
//                    lv_employee.setAdapter(employeeAdapter);
//                Toast.makeText(getApplicationContext(), node.getName(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
