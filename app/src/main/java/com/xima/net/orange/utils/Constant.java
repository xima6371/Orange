package com.xima.net.orange.utils;

public class Constant {
    //判断event是新增加还是修改
    public static final String EVENT_ACTION = "action_event";
    public static final String EVENT_ACTION_ADD = "action_event_add";
    public static final String EVENT_ACTION_MODIFY = "action_event_MODIFY";
    public static final String EVENT_LAST_TOP = "last_top_event";
    //权限请求码
    public static final int REQUEST_CODE_WRITE_PERMISSIONS = 0x1000;
    public static final int REQUEST_CODE_PICK_PHOTO = 0x1001;

    //event的类型
    public static final int EVENT_TYPE_ANNIVERSARY = 0x2000;
    public static final int EVENT_TYPE_BIRTHDAY = 0x2001;
    public static final int EVENT_TYPE_COUNTDOWN = 0x2002;

    //event封面图的路径
    public static final String EVENT_DEFAULT_PHOTO_PATH = "path";

    //RecyclerView的布局类型
    public static final int RECYCLER_TYPE_EMPTY = 0x3000;
    public static final int RECYCLER_TYPE_EVENT = 0x3001;

}
