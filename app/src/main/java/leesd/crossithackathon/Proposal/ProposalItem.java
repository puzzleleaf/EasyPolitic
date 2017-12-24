package leesd.crossithackathon.Proposal;

/**
 * Created by leesd on 2017-12-23.
 */

public class ProposalItem {

    private String number; // 글 번호
    private String title; // 글 제목
    private String institute; // 처리기관
    private String reportDate; // 신청일
    private String situation; // 처리상황


    public ProposalItem(){}

    public ProposalItem(String number, String title, String institute, String reportDate, String situation) {
        this.number = number;
        this.title = title;
        this.institute = institute;
        this.reportDate = reportDate;
        this.situation = situation;
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

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }



}
