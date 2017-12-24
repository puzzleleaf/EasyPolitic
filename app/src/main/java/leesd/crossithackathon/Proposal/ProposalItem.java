package leesd.crossithackathon.Proposal;

/**
 * Created by leesd on 2017-12-23.
 */

public class ProposalItem {

    private String number; // 글 번호
    private String title; // 글 제목
    private String institute; // 처리기관
    private String reportDate; // 신청일

    private String petiNo;
    private String anaCode;
    private String pageNo;
    private String reg_date1;
    private String reg_date2;


    public ProposalItem(){}
    public ProposalItem(String number, String title, String institute, String reportDate, String petiNo, String anaCode, String pageNo, String reg_date1, String reg_date2) {
        this.number = number;
        this.title = title;
        this.institute = institute;
        this.reportDate = reportDate;
        this.petiNo = petiNo;
        this.anaCode = anaCode;
        this.pageNo = pageNo;
        this.reg_date1 = reg_date1;
        this.reg_date2 = reg_date2;
    }
    public String getPetiNo() {
        return petiNo;
    }

    public void setPetiNo(String petiNo) {
        this.petiNo = petiNo;
    }

    public String getAnaCode() {
        return anaCode;
    }

    public void setAnaCode(String anaCode) {
        this.anaCode = anaCode;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getReg_date1() {
        return reg_date1;
    }

    public void setReg_date1(String reg_date1) {
        this.reg_date1 = reg_date1;
    }

    public String getReg_date2() {
        return reg_date2;
    }

    public void setReg_date2(String reg_date2) {
        this.reg_date2 = reg_date2;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }



}
