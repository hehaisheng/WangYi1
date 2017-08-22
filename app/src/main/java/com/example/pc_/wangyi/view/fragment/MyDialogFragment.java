package com.example.pc_.wangyi.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.pc_.wangyi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pc- on 2017/6/18.
 */
public class  MyDialogFragment extends DialogFragment implements View.OnClickListener {

    public IDialogListener iDialogListener;
    public Bundle bundle;
    Unbinder unbinder;


    @BindView(R.id.splash_queren)
    TextView splashQueren;
    @BindView(R.id.splash_quxiao)
    TextView splashQuxiao;

    @BindView(R.id.dialog_content)
    TextView dialogContent;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=inflater.inflate(R.layout.dialog_fragment,container,false);
        unbinder= ButterKnife.bind(this,view);
        bundle = this.getArguments();
        if (bundle != null) {
            String title = bundle.getString("dialogTitle");
            String content = bundle.getString("dialogcontent");

            if (content != null) {
                dialogContent.setText(content);
            }
        }


        splashQueren.setOnClickListener(this);
        splashQuxiao.setOnClickListener(this);
        return  view;
    }

    public static MyDialogFragment newInstance(Bundle bundle) {

        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.setArguments(bundle);
        return myDialogFragment;
    }


    public void setDialogListener(IDialogListener iDialogListener) {
        this.iDialogListener = iDialogListener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_queren:
                iDialogListener.confirm();
                break;
            case R.id.splash_quxiao:
                iDialogListener.cancel();
                break;
        }
    }


    public interface IDialogListener {
        void confirm();
        void cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bundle != null) {
            bundle = null;
        }
        if(unbinder!=null){
            unbinder.unbind();
        }

    }
}
