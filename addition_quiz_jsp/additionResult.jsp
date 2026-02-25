<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
Integer Nobj = (Integer) session.getAttribute("quizN");
int[] a = (int[]) session.getAttribute("quizA");
int[] b = (int[]) session.getAttribute("quizB");

if (Nobj == null || a == null || b == null) {
    response.sendRedirect("additionQuiz.jsp?new=1");
    return;
}

int N = Nobj;
int correctCount = 0;

Integer[] userAns = new Integer[N];
boolean[] isCorrect = new boolean[N];

for (int i = 0; i < N; i++) {
    String p = request.getParameter("ans" + i);
    int correct = a[i] + b[i];

    Integer parsed = null;
    if (p != null) {
        p = p.trim();
        if (!p.isEmpty()) {
            try {
                parsed = Integer.valueOf(p);
            } catch (NumberFormatException ex) {
                parsed = null;
            }
        }
    }

    userAns[i] = parsed;
    isCorrect[i] = (parsed != null && parsed == correct);

    if (isCorrect[i]) correctCount++;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quiz Results</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; }
table { border-collapse: collapse; width: 650px; }
th, td { border: 1px solid #ddd; padding: 10px; }
.ok { color: green; font-weight: bold; }
.bad { color: red; font-weight: bold; }
</style>
</head>
<body>

<h2>Results</h2>
<p>You got <b><%= correctCount %></b> out of <b><%= N %></b> correct.</p>

<table>
<tr>
<th>#</th>
<th>Question</th>
<th>Your Answer</th>
<th>Correct Answer</th>
<th>Result</th>
</tr>

<%
for (int i = 0; i < N; i++) {
int correct = a[i] + b[i];
%>
<tr>
<td><%= (i + 1) %></td>
<td><%= a[i] %> + <%= b[i] %></td>
<td><%= (userAns[i] == null ? "(blank)" : userAns[i]) %></td>
<td><%= correct %></td>
<td class="<%= isCorrect[i] ? "ok" : "bad" %>">
<%= isCorrect[i] ? "Correct" : "Wrong" %>
</td>
</tr>
<%
}
%>
</table>

<p><a href="additionQuiz.jsp?new=1">Try Again</a></p>

</body>
</html>
