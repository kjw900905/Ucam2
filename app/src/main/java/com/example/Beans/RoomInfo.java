package com.example.Beans;

public class RoomInfo {

    private String m_roomTitle;
    private String m_roomInterest;
    private String m_roomLimitMemberNumber;
    private String m_roomTime;
    private String m_roomCurrentMemberNumber;
    private String m_isEnterRoom;

    public RoomInfo(String roomTitle, String roomInterest, String roomLimitMemberNumber, String roomTime, String roomCurrentMemberNumber) {
        this.m_roomTitle = roomTitle;
        this.m_roomInterest = roomInterest;
        this.m_roomLimitMemberNumber = roomLimitMemberNumber;
        this.m_roomTime = roomTime;
        this.m_roomCurrentMemberNumber = roomCurrentMemberNumber;
        this.m_isEnterRoom = "T";
    }

    public RoomInfo() {

    }

    public void setM_roomTime(String m_roomTime) {
        this.m_roomTime = m_roomTime;
    }

    public String getM_roomTitle() {
        return m_roomTitle;
    }

    public void setM_roomTitle(String m_roomTitle) {
        this.m_roomTitle = m_roomTitle;
    }

    public String getM_roomInterest() {
        return m_roomInterest;
    }

    public void setM_roomInterest(String m_roomInterest) {
        this.m_roomInterest = m_roomInterest;
    }

    public String getM_roomLimitMemberNumber() {
        return m_roomLimitMemberNumber;
    }

    public void setM_roomLimitMemberNumber(String m_roomLimitMemberNumber) {
        this.m_roomLimitMemberNumber = m_roomLimitMemberNumber;
    }

    public String getM_roomTime() {
        return m_roomTime;
    }

    public void setM_roomCurrentMemberNumber(String m_roomCurrentMemberNumber){
        this.m_roomCurrentMemberNumber = m_roomCurrentMemberNumber;
    }

    public String getM_roomCurrentMemberNumber(){
        return m_roomCurrentMemberNumber;
    }

    public void setM_isEnterRoom(String m_isEnterRoom) {
        this.m_isEnterRoom = m_isEnterRoom;
    }

    public String getM_isEnterRoom() {
        return m_isEnterRoom;
    }
}
