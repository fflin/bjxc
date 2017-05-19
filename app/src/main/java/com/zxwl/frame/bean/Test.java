package com.zxwl.frame.bean;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/5/5 19:31
 * ClassName: ${Class_Name}
 */

public class Test {

    /**
     * terminalName : 邓卓海
     * mcuName :
     * siteId : 3466
     * unitName : 襄城
     * sound :
     * administrator : 书记
     * lastState : -1
     * siteStatus : {"videoSource":"","status":"3","isDataOnline":"","type":"7","uri":"71031001","mcuId":"","isLocalVideoOpen":"0","name":"邓卓海","volume":"65535","screens":"0","participantType":"0","isMute":"1","callFailedReason":{"errCode":"110","discription":""},"isQuiet":"1"}
     * unitId : 6497
     * siteInfo : {"rate":"1920k","status":"","name":"邓卓海","isLockVideoSource":"","videoProtocol":"","videoFormat":"","participantType":"","from":"0","dialingMode":"0","type":"7","dtmf":"","uri":"71031001","mcuId":""}
     * mobile :
     * ip :
     */

    public String terminalName;
    public String mcuName;
    public String siteId;
    public String unitName;
    public String sound;
    public String administrator;
    public String lastState;
    public SiteStatusBean siteStatus;
    public String unitId;
    public SiteInfoBean siteInfo;
    public String mobile;
    public String ip;

    public static class SiteStatusBean {
        /**
         * videoSource :
         * status : 3
         * isDataOnline :
         * type : 7
         * uri : 71031001
         * mcuId :
         * isLocalVideoOpen : 0
         * name : 邓卓海
         * volume : 65535
         * screens : 0
         * participantType : 0
         * isMute : 1
         * callFailedReason : {"errCode":"110","discription":""}
         * isQuiet : 1
         */

        public String videoSource;
        public String status;
        public String isDataOnline;
        public String type;
        public String uri;
        public String mcuId;
        public String isLocalVideoOpen;
        public String name;
        public String volume;
        public String screens;
        public String participantType;
        public String isMute;
        public CallFailedReasonBean callFailedReason;
        public String isQuiet;

        public static class CallFailedReasonBean {
            /**
             * errCode : 110
             * discription :
             */
            public String errCode;
            public String discription;
        }
    }

    public static class SiteInfoBean {
        /**
         * rate : 1920k
         * status :
         * name : 邓卓海
         * isLockVideoSource :
         * videoProtocol :
         * videoFormat :
         * participantType :
         * from : 0
         * dialingMode : 0
         * type : 7
         * dtmf :
         * uri : 71031001
         * mcuId :
         */

        public String rate;
        public String status;
        public String name;
        public String isLockVideoSource;
        public String videoProtocol;
        public String videoFormat;
        public String participantType;
        public String from;
        public String dialingMode;
        public String type;
        public String dtmf;
        public String uri;
        public String mcuId;
    }
}
