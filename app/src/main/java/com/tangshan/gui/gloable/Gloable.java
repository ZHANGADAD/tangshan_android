package com.tangshan.gui.gloable;

import java.io.File;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tangshan.gui.MApplication;

public class Gloable {

    public static final File FILE_IMAGE_PATH = StorageUtils
            .getOwnCacheDirectory(MApplication.getInstance(),
                    "/tangshanapp/images/");
    public static final String SHARRDESCRIPTOR = "SHARRDESCRIPTOR";
    public static final String TIANQIUTL = "forecast_o.php";
    public static final String GAOSUURL = "HBExpwy.php";
    public static final String KONGQIZHILIANG = "HBAQI_o.php";
    public static final String YUJINGURL = "HBalarm.php";
    public static final String SHENGHUOQIXIANGURL = "HBMeteoIndex_o.php";
    public static final String NONGYEURL = "HBAQI_oo.php";
    public static final String GUOQU24URL = "pre24h.php";
    public static final float MapZoomLevel = 12;
    public static final String NONGYEDETAILSURL = null;
    public static final String JIAOTONGURL = "HBExpwy.php";
    public static final long EXPIRETIME = 3600 * 1000;
    public static final String LUKUANGURL = "RoadComprehensive.php";
    public static final String XINGCHEURL = "driving.php";
    public static final String INFOURL = "config_syn.php";
    public static final String KANDIANURL = "sx.php";
    public static final String ADafaFSurl = "HBExpwyOff.php";

    public static String appKey = "rhj4thzsu7bgpybua3qzspaad6r5x7erjlh6vay2";

    public static String UPDATEURL = "getVersion.php";
}
