package com.zxwl.frame.bean;

import java.io.Serializable;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/26 09:12
 * 会议的实体bean
 */
public class ConfBean implements Serializable {
    // Fields
    public String id;//id
    public String smcConfId;//会议ID
    public String beginTime;//会议开始时间
    //（由于建成后不是存储过程改动 要么就是显示和后台中改动，不方便所以做此改动）
    public String factBeginTime;//实际会议开始时间
    public String endTime;//预约会议结束时间
    public String factEndTime;//实际会议结束时间
    public String schedulingTime;//会议实际调度时间
    public String duration;//会议时长（单位分钟）
    public String createTime;//创建时间
    public String name;//会议名称
    public String peopleIdOa;//申请人id
    public String applyPeople;//会议申请人
    public String deptId;//主办部门id
    public String applyDept;//主办部门
    public String unitId;//主办单位id
    public String unit;//所属单位
    public String unitIdName;//
    public String oneArea;//一级区域
    public String twoArea;//二级区域
    public String threeArea;//三级区域
    public String fourArea;//四级区域
    public String confType;//会议类型  0=周期性视频会议、1=周期性非视频会议、2=视频会议、3=非视频会议、4=即时视频会议、5=即时非视频会议 6、周期性子视频会议 7、周期性子非视频会议
    public String confState;//会议状态 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
    public String confMode;//会议模式 0：宣导模式		1：讨论模式
    public String confRange;//参会范围
    public String remarks;//会议备注
    //public String auditState;//审批状态 0：等待审批 1：审批通过 2：审批失败
    public String vetos;//审批意见
    public String checkPeopleIdOa;//审批人id
    public String checkPeople;//会议审批人
    public String checkTime;//审批时间
    public String staffCount;//参会人员数
    public String siteCount;//参会会场数
    public String state;//标记删除状态
    public String isEmail;//是否需要邮件 0：发送 1或者空为不需要发送
    public String isSms;//是否需要短信 0：发送 1或者空为不需要发送
    public String emailId;//邮件模版id
    public String emailTitle;//邮件模板标题
    public String emailContext;//邮件模版内容
    public String smsId;//短信模版id
    public String smsTitle;//短信模版标题
    public String smsContext;//短信模版内容
    public String isFirst;//时间是否从第一个值开始
    public String confExplain;//说明
    /*--sy add 2014- 8-9- ConfParameters表*/
    public String confParameters;//ConfParameters表id
    public String accessCode;//会议接入码
    public String password;//会议接入密码
    public String mediaEncrypptType;//媒体流加密方式
    public String auxVideoFormat;//辅流视频格式
    public String auxVideoProtocol;//辅流视频协议
    public String cpResouce;//多画面资源数
    public String rate;//速率
    public String isRecording;//会议是否支持录制
    public String recorderAddr;//录播地址
    public String isLiveBroadcast;//录播会议是否支持直播功能，默认不支持
    public String presentation;//演示方式
    public String chairmanPassword;//主席密码或会议启动密码
    public String billCode;//计费码
    public String confException;//会议异常错误
    public String isKeepSecret;//是否保密(0:不保密 ，1：保密);
    public String departPath;//部门路径
    public String errorCode;//会议调度失败错误码
    public String cancelConfExplain;//错误原因
    public String parentId;//父节点id
    public String contactPeople;//联系人
    public String contactTelephone;//联系电话
    public String organizer;//承办部门

    public String deviceNumber;//设备数量
    public String deviceName;//设备名称
    public boolean select;//复选框是否选中
    public boolean showControl = false;//是否显示控制界面

    public boolean isVegetarian;//收缩

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfBean confBean = (ConfBean) o;

        if (id != null ? !id.equals(confBean.id) : confBean.id != null) return false;
        if (smcConfId != null ? !smcConfId.equals(confBean.smcConfId) : confBean.smcConfId != null)
            return false;
        if (beginTime != null ? !beginTime.equals(confBean.beginTime) : confBean.beginTime != null)
            return false;
        if (factBeginTime != null ? !factBeginTime.equals(confBean.factBeginTime) : confBean.factBeginTime != null)
            return false;
        if (endTime != null ? !endTime.equals(confBean.endTime) : confBean.endTime != null)
            return false;
        if (factEndTime != null ? !factEndTime.equals(confBean.factEndTime) : confBean.factEndTime != null)
            return false;
        if (schedulingTime != null ? !schedulingTime.equals(confBean.schedulingTime) : confBean.schedulingTime != null)
            return false;
        if (duration != null ? !duration.equals(confBean.duration) : confBean.duration != null)
            return false;
        if (createTime != null ? !createTime.equals(confBean.createTime) : confBean.createTime != null)
            return false;
        if (name != null ? !name.equals(confBean.name) : confBean.name != null) return false;
        if (peopleIdOa != null ? !peopleIdOa.equals(confBean.peopleIdOa) : confBean.peopleIdOa != null)
            return false;
        if (applyPeople != null ? !applyPeople.equals(confBean.applyPeople) : confBean.applyPeople != null)
            return false;
        if (deptId != null ? !deptId.equals(confBean.deptId) : confBean.deptId != null)
            return false;
        if (applyDept != null ? !applyDept.equals(confBean.applyDept) : confBean.applyDept != null)
            return false;
        if (unitId != null ? !unitId.equals(confBean.unitId) : confBean.unitId != null)
            return false;
        if (unit != null ? !unit.equals(confBean.unit) : confBean.unit != null) return false;
        if (unitIdName != null ? !unitIdName.equals(confBean.unitIdName) : confBean.unitIdName != null)
            return false;
        if (oneArea != null ? !oneArea.equals(confBean.oneArea) : confBean.oneArea != null)
            return false;
        if (twoArea != null ? !twoArea.equals(confBean.twoArea) : confBean.twoArea != null)
            return false;
        if (threeArea != null ? !threeArea.equals(confBean.threeArea) : confBean.threeArea != null)
            return false;
        if (fourArea != null ? !fourArea.equals(confBean.fourArea) : confBean.fourArea != null)
            return false;
        if (confType != null ? !confType.equals(confBean.confType) : confBean.confType != null)
            return false;
        if (confState != null ? !confState.equals(confBean.confState) : confBean.confState != null)
            return false;
        if (confMode != null ? !confMode.equals(confBean.confMode) : confBean.confMode != null)
            return false;
        if (confRange != null ? !confRange.equals(confBean.confRange) : confBean.confRange != null)
            return false;
        if (remarks != null ? !remarks.equals(confBean.remarks) : confBean.remarks != null)
            return false;
        if (vetos != null ? !vetos.equals(confBean.vetos) : confBean.vetos != null) return false;
        if (checkPeopleIdOa != null ? !checkPeopleIdOa.equals(confBean.checkPeopleIdOa) : confBean.checkPeopleIdOa != null)
            return false;
        if (checkPeople != null ? !checkPeople.equals(confBean.checkPeople) : confBean.checkPeople != null)
            return false;
        if (checkTime != null ? !checkTime.equals(confBean.checkTime) : confBean.checkTime != null)
            return false;
        if (staffCount != null ? !staffCount.equals(confBean.staffCount) : confBean.staffCount != null)
            return false;
        if (siteCount != null ? !siteCount.equals(confBean.siteCount) : confBean.siteCount != null)
            return false;
        if (state != null ? !state.equals(confBean.state) : confBean.state != null) return false;
        if (isEmail != null ? !isEmail.equals(confBean.isEmail) : confBean.isEmail != null)
            return false;
        if (isSms != null ? !isSms.equals(confBean.isSms) : confBean.isSms != null) return false;
        if (emailId != null ? !emailId.equals(confBean.emailId) : confBean.emailId != null)
            return false;
        if (emailTitle != null ? !emailTitle.equals(confBean.emailTitle) : confBean.emailTitle != null)
            return false;
        if (emailContext != null ? !emailContext.equals(confBean.emailContext) : confBean.emailContext != null)
            return false;
        if (smsId != null ? !smsId.equals(confBean.smsId) : confBean.smsId != null) return false;
        if (smsTitle != null ? !smsTitle.equals(confBean.smsTitle) : confBean.smsTitle != null)
            return false;
        if (smsContext != null ? !smsContext.equals(confBean.smsContext) : confBean.smsContext != null)
            return false;
        if (isFirst != null ? !isFirst.equals(confBean.isFirst) : confBean.isFirst != null)
            return false;
        if (confExplain != null ? !confExplain.equals(confBean.confExplain) : confBean.confExplain != null)
            return false;
        if (confParameters != null ? !confParameters.equals(confBean.confParameters) : confBean.confParameters != null)
            return false;
        if (accessCode != null ? !accessCode.equals(confBean.accessCode) : confBean.accessCode != null)
            return false;
        if (password != null ? !password.equals(confBean.password) : confBean.password != null)
            return false;
        if (mediaEncrypptType != null ? !mediaEncrypptType.equals(confBean.mediaEncrypptType) : confBean.mediaEncrypptType != null)
            return false;
        if (auxVideoFormat != null ? !auxVideoFormat.equals(confBean.auxVideoFormat) : confBean.auxVideoFormat != null)
            return false;
        if (auxVideoProtocol != null ? !auxVideoProtocol.equals(confBean.auxVideoProtocol) : confBean.auxVideoProtocol != null)
            return false;
        if (cpResouce != null ? !cpResouce.equals(confBean.cpResouce) : confBean.cpResouce != null)
            return false;
        if (rate != null ? !rate.equals(confBean.rate) : confBean.rate != null) return false;
        if (isRecording != null ? !isRecording.equals(confBean.isRecording) : confBean.isRecording != null)
            return false;
        if (recorderAddr != null ? !recorderAddr.equals(confBean.recorderAddr) : confBean.recorderAddr != null)
            return false;
        if (isLiveBroadcast != null ? !isLiveBroadcast.equals(confBean.isLiveBroadcast) : confBean.isLiveBroadcast != null)
            return false;
        if (presentation != null ? !presentation.equals(confBean.presentation) : confBean.presentation != null)
            return false;
        if (chairmanPassword != null ? !chairmanPassword.equals(confBean.chairmanPassword) : confBean.chairmanPassword != null)
            return false;
        if (billCode != null ? !billCode.equals(confBean.billCode) : confBean.billCode != null)
            return false;
        if (confException != null ? !confException.equals(confBean.confException) : confBean.confException != null)
            return false;
        if (isKeepSecret != null ? !isKeepSecret.equals(confBean.isKeepSecret) : confBean.isKeepSecret != null)
            return false;
        if (departPath != null ? !departPath.equals(confBean.departPath) : confBean.departPath != null)
            return false;
        if (errorCode != null ? !errorCode.equals(confBean.errorCode) : confBean.errorCode != null)
            return false;
        if (cancelConfExplain != null ? !cancelConfExplain.equals(confBean.cancelConfExplain) : confBean.cancelConfExplain != null)
            return false;
        if (parentId != null ? !parentId.equals(confBean.parentId) : confBean.parentId != null)
            return false;
        if (contactPeople != null ? !contactPeople.equals(confBean.contactPeople) : confBean.contactPeople != null)
            return false;
        if (contactTelephone != null ? !contactTelephone.equals(confBean.contactTelephone) : confBean.contactTelephone != null)
            return false;
        if (organizer != null ? !organizer.equals(confBean.organizer) : confBean.organizer != null)
            return false;
        if (deviceNumber != null ? !deviceNumber.equals(confBean.deviceNumber) : confBean.deviceNumber != null)
            return false;
        return deviceName != null ? deviceName.equals(confBean.deviceName) : confBean.deviceName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (smcConfId != null ? smcConfId.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (factBeginTime != null ? factBeginTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (factEndTime != null ? factEndTime.hashCode() : 0);
        result = 31 * result + (schedulingTime != null ? schedulingTime.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (peopleIdOa != null ? peopleIdOa.hashCode() : 0);
        result = 31 * result + (applyPeople != null ? applyPeople.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        result = 31 * result + (applyDept != null ? applyDept.hashCode() : 0);
        result = 31 * result + (unitId != null ? unitId.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (unitIdName != null ? unitIdName.hashCode() : 0);
        result = 31 * result + (oneArea != null ? oneArea.hashCode() : 0);
        result = 31 * result + (twoArea != null ? twoArea.hashCode() : 0);
        result = 31 * result + (threeArea != null ? threeArea.hashCode() : 0);
        result = 31 * result + (fourArea != null ? fourArea.hashCode() : 0);
        result = 31 * result + (confType != null ? confType.hashCode() : 0);
        result = 31 * result + (confState != null ? confState.hashCode() : 0);
        result = 31 * result + (confMode != null ? confMode.hashCode() : 0);
        result = 31 * result + (confRange != null ? confRange.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (vetos != null ? vetos.hashCode() : 0);
        result = 31 * result + (checkPeopleIdOa != null ? checkPeopleIdOa.hashCode() : 0);
        result = 31 * result + (checkPeople != null ? checkPeople.hashCode() : 0);
        result = 31 * result + (checkTime != null ? checkTime.hashCode() : 0);
        result = 31 * result + (staffCount != null ? staffCount.hashCode() : 0);
        result = 31 * result + (siteCount != null ? siteCount.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (isEmail != null ? isEmail.hashCode() : 0);
        result = 31 * result + (isSms != null ? isSms.hashCode() : 0);
        result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
        result = 31 * result + (emailTitle != null ? emailTitle.hashCode() : 0);
        result = 31 * result + (emailContext != null ? emailContext.hashCode() : 0);
        result = 31 * result + (smsId != null ? smsId.hashCode() : 0);
        result = 31 * result + (smsTitle != null ? smsTitle.hashCode() : 0);
        result = 31 * result + (smsContext != null ? smsContext.hashCode() : 0);
        result = 31 * result + (isFirst != null ? isFirst.hashCode() : 0);
        result = 31 * result + (confExplain != null ? confExplain.hashCode() : 0);
        result = 31 * result + (confParameters != null ? confParameters.hashCode() : 0);
        result = 31 * result + (accessCode != null ? accessCode.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (mediaEncrypptType != null ? mediaEncrypptType.hashCode() : 0);
        result = 31 * result + (auxVideoFormat != null ? auxVideoFormat.hashCode() : 0);
        result = 31 * result + (auxVideoProtocol != null ? auxVideoProtocol.hashCode() : 0);
        result = 31 * result + (cpResouce != null ? cpResouce.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + (isRecording != null ? isRecording.hashCode() : 0);
        result = 31 * result + (recorderAddr != null ? recorderAddr.hashCode() : 0);
        result = 31 * result + (isLiveBroadcast != null ? isLiveBroadcast.hashCode() : 0);
        result = 31 * result + (presentation != null ? presentation.hashCode() : 0);
        result = 31 * result + (chairmanPassword != null ? chairmanPassword.hashCode() : 0);
        result = 31 * result + (billCode != null ? billCode.hashCode() : 0);
        result = 31 * result + (confException != null ? confException.hashCode() : 0);
        result = 31 * result + (isKeepSecret != null ? isKeepSecret.hashCode() : 0);
        result = 31 * result + (departPath != null ? departPath.hashCode() : 0);
        result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
        result = 31 * result + (cancelConfExplain != null ? cancelConfExplain.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (contactPeople != null ? contactPeople.hashCode() : 0);
        result = 31 * result + (contactTelephone != null ? contactTelephone.hashCode() : 0);
        result = 31 * result + (organizer != null ? organizer.hashCode() : 0);
        result = 31 * result + (deviceNumber != null ? deviceNumber.hashCode() : 0);
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        return result;
    }

    //    public String select;//复选框是否选中

//    // Fields
//    private Integer id;//id
//    private String smcConfId;//会议ID
//    private String beginTime;//会议开始时间
//    //（由于建成后不是存储过程改动 要么就是显示和后台中改动，不方便所以做此改动）
//    private String factBeginTime;//实际会议开始时间
//    private String endTime;//预约会议结束时间
//    private String factEndTime;//实际会议结束时间
//    private String schedulingTime;//会议实际调度时间
//    private Integer duration;//会议时长（单位分钟）
//    private String createTime;//创建时间
//    private String name;//会议名称
//    private Integer peopleIdOa;//申请人id
//    private String applyPeople;//会议申请人
//    private Integer deptId;//主办部门id
//    private String applyDept;//主办部门
//    private Integer unitId;//主办单位id
//    private String unit;//所属单位
//    private Integer oneArea;//一级区域
//    private Integer twoArea;//二级区域
//    private Integer threeArea;//三级区域
//    private Integer fourArea;//四级区域
//    private Integer confType;//会议类型  0=周期性视频会议、1=周期性非视频会议、2=视频会议、3=非视频会议、4=即时视频会议、5=即时非视频会议 6、周期性子视频会议 7、周期性子非视频会议
//    private Integer confState;//会议状态 0=暂存 1=取消会议  10=等待审批 15=审批超时 20=等待召开 30=正在召开 40=会议异常  50=会议结束 5=审批驳回 （原来为：0：等待召开 1：正在召开 2：召开完毕  3 ：暂存  ）
//    private Integer confMode;//会议模式 0：宣导模式		1：讨论模式
//    private String confRange;//参会范围
//    private String remarks;//会议备注
//    //private Integer auditState;//审批状态 0：等待审批 1：审批通过 2：审批失败
//    private String vetos;//审批意见
//    private Integer checkPeopleIdOa;//审批人id
//    private String checkPeople;//会议审批人
//    private String checkTime;//审批时间
//    private Integer staffCount;//参会人员数
//    private Integer siteCount;//参会会场数
//    private Integer state;//标记删除状态
//    private Integer isEmail;//是否需要邮件 0：发送 1或者空为不需要发送
//    private Integer isSms;//是否需要短信 0：发送 1或者空为不需要发送
//    private Integer emailId;//邮件模版id
//    private String emailTitle;//邮件模板标题
//    private String emailContext;//邮件模版内容
//    private Integer smsId;//短信模版id
//    private String smsTitle;//短信模版标题
//    private String smsContext;//短信模版内容
//    private Integer isFirst;//时间是否从第一个值开始
//    private String confExplain;//说明
//    /*--sy add 2014- 8-9- ConfParameters表*/
//    private Integer confParameters;//ConfParameters表id
//    private String accessCode;//会议接入码
//    private String password;//会议接入密码
//    private Integer mediaEncrypptType;//媒体流加密方式
//    private Integer auxVideoFormat;//辅流视频格式
//    private Integer auxVideoProtocol;//辅流视频协议
//    private Integer cpResouce;//多画面资源数
//    private String rate;//速率
//    private Integer isRecording;//会议是否支持录制
//    private String recorderAddr;//录播地址
//    private Integer isLiveBroadcast;//录播会议是否支持直播功能，默认不支持
//    private Integer presentation;//演示方式
//    private String chairmanPassword;//主席密码或会议启动密码
//    private String billCode;//计费码
//    private String confException;//会议异常错误
//    private Integer isKeepSecret;//是否保密(0:不保密 ，1：保密);
//    private String departPath;//部门路径
//    private Integer errorCode;//会议调度失败错误码
//
//    private Integer cancelConfExplain;//错误原因
//
//    private Integer parentId;//父节点id
//    private String contactPeople;//联系人
//    private String contactTelephone;//联系电话
//    private String organizer;//承办部门
}
