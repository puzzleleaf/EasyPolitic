package leesd.crossithackathon.DataManager;

/**
 * Created by hbLee on 2017-08-06.
 */

public class CivilComplaintRateVO {

    String institution_name="";
    int total_register;
    int in_date_handling;
    int out_date_handling;
    int out_date_failure;
    double handling_rate;

    public CivilComplaintRateVO(String institution_name, int total_register, int in_date_handling, int out_date_handling, int out_date_failure, double handling_rate){

        setInstitution_name(institution_name);
        setTotal_register(total_register);
        setIn_date_handling(in_date_handling);
        setOut_date_handling(out_date_handling);
        setOut_date_failure(out_date_failure);
        setHandling_rate(handling_rate);

    }
    public CivilComplaintRateVO(String institution_name){

        setInstitution_name(institution_name);
        setTotal_register(total_register);
        setIn_date_handling(in_date_handling);
        setOut_date_handling(out_date_handling);
        setOut_date_failure(out_date_failure);
        setHandling_rate(handling_rate);

    }
    public String getInstitution_name() {
        return institution_name;
    }

    public void setInstitution_name(String institution_name) {
        this.institution_name = institution_name;
    }

    public int getTotal_register() {
        return total_register;
    }

    public void setTotal_register(int total_register) {
        this.total_register = total_register;
    }

    public int getIn_date_handling() {
        return in_date_handling;
    }

    public void setIn_date_handling(int in_date_handling) {
        this.in_date_handling = in_date_handling;
    }

    public int getOut_date_handling() {
        return out_date_handling;
    }

    public void setOut_date_handling(int out_date_handling) {
        this.out_date_handling = out_date_handling;
    }

    public int getOut_date_failure() {
        return out_date_failure;
    }

    public void setOut_date_failure(int out_date_failure) {
        this.out_date_failure = out_date_failure;
    }

    public double getHandling_rate() {
        return handling_rate;
    }

    public void setHandling_rate(double handling_rate) {
        this.handling_rate = handling_rate;
    }

    public String toString(){

        return institution_name+"/" +total_register+"/" +in_date_handling+"/" +out_date_handling+"/" +out_date_failure+"/" +handling_rate;
    }
}
