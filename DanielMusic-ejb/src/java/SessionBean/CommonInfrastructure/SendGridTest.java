/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.CommonInfrastructure;

import com.sendgrid.SendGrid;

/**
 *
 * @author -VeRyLuNaTiC
 */
public class SendGridTest {

    public static void main(String[] args) {
        System.out.println("CommonInfrastructureBean: sendEmail() called");
        try {
            SendGrid sendgrid = new SendGrid("sounds.sg", "gfhdRVA^%@Ruk3gyj");
            SendGrid.Email email = new SendGrid.Email();
            email.addTo("leeyuanguang.lyg@gmail.com");
            email.setFrom("no-reply@sounds.sg");
            email.setFromName("Sounds.sg");
            email.setSubject("Subject");
            email.setText("test message content");
            SendGrid.Response respose = sendgrid.send(email);
            System.out.println(respose.getMessage());
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: sendEmail() failed");
            ex.printStackTrace();
        }
    }
}
