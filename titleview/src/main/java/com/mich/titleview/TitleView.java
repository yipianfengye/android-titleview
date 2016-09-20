package com.mich.titleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by aaron on 16/9/19.
 * 自定义实现TitleView组件
 */
public class TitleView extends FrameLayout{

    /**
     * title TextView组件
     */
    private TextView firstTitle = null;
    /**
     * title TextView组件
     */
    private TextView secondTitle = null;
    /**
     * 播放次数TextView组件
     */
    private TextView countView = null;

    /**
     * title组件
     */
    private String titleContent = "";
    private String countContent = "";

    public TitleView(Context context) {
        super(context);

        init(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    /**
     * 执行初始化操作
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        if (context == null) {
            return;
        }

        View rootView = LayoutInflater.from(context).inflate(R.layout.view_titleview, null);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(rootView, lps);

        /**
         * 初始化组件
         */
        initView(rootView);

        /**
         * 初始化自定义属性
         */
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.titleview);
            titleContent = ta.getString(R.styleable.titleview_title_content);
            countContent = ta.getString(R.styleable.titleview_count_content);
            firstTitle.setText(titleContent);
            countView.setText(countContent);

            float titleTextSize = ta.getDimension(R.styleable.titleview_title_text_size, firstTitle.getTextSize());
            firstTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            secondTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

            int titleTextColor = ta.getColor(R.styleable.titleview_title_text_color, firstTitle.getCurrentTextColor());
            firstTitle.setTextColor(titleTextColor);
            secondTitle.setTextColor(titleTextColor);

            float countTextSize = ta.getDimension(R.styleable.titleview_count_text_size, countView.getTextSize());
            countView.setTextSize(TypedValue.COMPLEX_UNIT_PX, countTextSize);

            int countTextColor = ta.getColor(R.styleable.titleview_count_text_color, countView.getCurrentTextColor());
            countView.setTextColor(countTextColor);
        }

    }


    /**
     * 执行组件的初始化操作
     * @param rootView
     */
    private void initView(View rootView) {
        firstTitle = (TextView) rootView.findViewById(R.id.first_textview);
        secondTitle = (TextView) rootView.findViewById(R.id.second_textview);
        countView = (TextView) rootView.findViewById(R.id.count_textview);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        doUpdateUI();
    }

    /**
     * 执行更新UI的操作
     */
    private void doUpdateUI() {
        if (firstTitle != null) {
            firstTitle.post(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 当title只有一行时,不做任何操作,默认即可
                     */
                    Paint paint = firstTitle.getPaint();
                    if (paint.measureText(titleContent) <= firstTitle.getWidth()) {
                        secondTitle.setVisibility(View.GONE);
                    }
                    /**
                     * 执行title显示为两行的处理逻辑
                     */
                    else {
                        secondTitle.setVisibility(View.VISIBLE);
                        /**
                         * 获取第一行显示内容,第二行显示内容
                         */
                        int count = 1;
                        StringBuffer sb = new StringBuffer(titleContent.substring(0, 1));
                        while (paint.measureText(sb.toString()) < firstTitle.getWidth()) {
                            if (count >= titleContent.length()) {
                                break;
                            }
                            sb.append(titleContent.substring(count, count + 1));
                            count ++;
                        }
                        String firstLineContent = sb.toString();
                        String secondLineContent = titleContent.substring(count);

                        firstTitle.setText(firstLineContent);
                        secondTitle.setText(secondLineContent);

                        /**
                         * 获取相关组件的宽高
                         */
                        int titleWidth = firstTitle.getWidth();
                        int countWidth = countView.getWidth();

                        /**
                         * 获取第二行文字的长度
                         */
                        float secondLineWidth = secondTitle.getPaint().measureText(secondLineContent);

                        /**
                         * 判断第二行文字长度是否大于组件的长度-播放次数组件的长度
                         */
                        if (secondLineWidth > titleWidth - countWidth) {
                            secondTitle.getLayoutParams().width = titleWidth - countWidth;
                        } else {
                            secondTitle.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }

                        secondTitle.setText(secondLineContent);
                    }
                }
            });
        }
    }


    // ###################### 组件的set get方法 #########################

    public String getTitleContent() {
        return new StringBuffer(firstTitle.getText().toString()).append(secondTitle.getText().toString()).toString();
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;

        firstTitle.setText(titleContent);
        doUpdateUI();
    }

    public String getCountContent() {
        return countView.getText().toString();
    }

    public void setCountContent(String countContent) {
        this.countContent = countContent;
        countView.setText(countContent);
        doUpdateUI();
    }
}
