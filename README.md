# android-titleview

在介绍具体的使用说明之前，我们先看一下简单的实现效果：
<br>![这里写图片描述](http://img.blog.csdn.net/20160920111903431)<br>

**使用说明**

- 当大title文案显示为一行的时候，大title和小title分行显示；

- 当大title文案显示为两行且不满两行的时候，小title在大title第二行显示内容之后显示；

- 当大title文案显示满两行的时候，小title在第二行末尾显示，大title第二行不占满全行，切末尾以...结束；

- 支持对大title文案内容，字体颜色，字体大小的设定，支持对小title文案内容，字体颜色，字体大小的设定；

**自定义属性说明：**

```
<!-- titleView自定义属性 -->
    <declare-styleable name="titleview" >
	    <!-- 用于设定大title的文案内容 -->
        <attr name="title_content" format="string"/>
        <!-- 用于设定小title的文案内容 -->
        <attr name="count_content" format="string"/>
        <!-- 用于设定大title文案文字大小 -->
        <attr name="title_text_size" format="dimension"/>
        <!-- 用于设定大title文案文字颜色 -->
        <attr name="title_text_color" format="color"/>
        <!-- 用于设定小title文案文字大小 -->
        <attr name="count_text_size" format="dimension"/>
        <!-- 用于设定小title文案文字颜色 -->
        <attr name="count_text_color" format="color"/>
    </declare-styleable>
```

**使用方式：**

- 在module的build.gradle中执行compile操作

```
compile 'cn.yipianfengye.android:mich-titleview:1.0'
```

- 在Layout布局文件中定义组件

```
<com.mich.titleview.TitleView
        android:id="@+id/title_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:padding="10dp"
        app:title_content="人鱼线,狗公腰,刚果队长真是太帅了"
        app:count_content="15.3万次播放"
        app:title_text_size="18dp"
        app:title_text_color="#334455"
        app:count_text_size="12dp"
        app:count_text_color="#666666"
        />
```


- 在代码中管理自定义组件

```
/**
     * 初始化组件
     */
    public void initView() {
        titleView = (TitleView) findViewById(R.id.title_view);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setTitleContent(titleView.getTitleContent() + "人鱼线,狗公腰,刚过队长太帅了");
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getTitleContent().length() > 15) {
                    titleView.setTitleContent(titleView.getTitleContent().substring(0, titleView.getTitleContent().length() - 15));
                }
            }
        });
    }
```

怎么样是不是很简单？下面我们可以来看一下具体API。

**实现原理：**

下面是自定义组件的布局实现部分：

```
<?xml version="1.0" encoding="utf-8"?>
<marge xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <TextView
        android:id="@+id/first_textview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18dp"
        android:textColor="#334455"
        android:maxLines="1"
        />

    <LinearLayout
        android:id="@+id/linear_inner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginTop="8dp"
        >

        <TextView
            android:id="@+id/second_textview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="18dp"
            android:textColor="#334455"
            android:maxLines="1"
            android:layout_gravity="center_vertical"
            android:ellipsize="end"
            />

        <TextView
            android:id="@+id/count_textview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="15.2万次播放"
            android:textSize="12dp"
            android:maxLines="1"
            android:ellipsize="end"
            android:layout_gravity="center_vertical"
            />
    </LinearLayout>

</marge>
```

下面是自定义组件的核心代码部分：

```
/**
 * Created by aaron on 16/9/19.
 * 自定义实现TitleView组件
 */
public class TitleView extends LinearLayout{

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
```

可以看到注释的挺详细的，其主要的实现思路是：通过三个TextView实现的：

- 首先判断大title的内容是否占满一行，若没有的话，则大小title控件分行显示；

- 其次截取大title第一行的显示本文显示在第一行的控件上，然后获取剩余的显示文本显示在第二个控件上，若第二个控件也将占满一行则重设其宽度，预留出可以显示小title控件的位置；

当然更具体的关于控件的实现可以下载源码参考。
