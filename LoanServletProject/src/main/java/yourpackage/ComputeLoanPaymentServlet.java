
package yourpackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;

@WebServlet("/ComputeLoanPayment")
public class ComputeLoanPaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        String loanAmountStr = req.getParameter("loanAmount");
        String rateStr = req.getParameter("annualInterestRate");
        String yearsStr = req.getParameter("numberOfYears");

        try (PrintWriter out = resp.getWriter()) {
            double loanAmount = Double.parseDouble(loanAmountStr);
            double annualRate = Double.parseDouble(rateStr);
            int years = Integer.parseInt(yearsStr);

            Loan loan = new Loan(annualRate, years, loanAmount);

            NumberFormat currency = NumberFormat.getCurrencyInstance();

            out.println("<html><body>");
            out.println("<h2>Loan Payment Results</h2>");
            out.println("<p>Monthly Payment: " + currency.format(loan.getMonthlyPayment()) + "</p>");
            out.println("<p>Total Payment: " + currency.format(loan.getTotalPayment()) + "</p>");
            out.println("<p><a href='loanForm.html'>Back</a></p>");
            out.println("</body></html>");
        }
    }
}
