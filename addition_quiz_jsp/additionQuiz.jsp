<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    final int N = 10;

    String newQuiz = request.getParameter("new");
    boolean forceNew = "1".equals(newQuiz);

    int[] a = (int[]) session.getAttribute("quizA");
    int[] b = (int[]) session.getAttribute("quizB");

    if (forceNew || a == null || b == null || a.length != N || b.length != N) {
        a = new int[N];
        b = new int[N];
        Random r = new Random();

        for (int i = 0; i < N; i++) {
            a[i] = r.nextInt(10);
            b[i] = r.nextInt(10);
        }

        session.setAttribute("quizA", a);
        session.setAttribute("quizB", b);
        session.setAttribute("quizN", N);
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Addition Quiz</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; }
table { border-collapse: collapse; }
td { padding: 8px 12px; }
input[type="text"] { width: 80px; padding: 6px; }
.btn { padding: 8px 12px; cursor: pointer; }
.actions { margin-top: 16px; display: flex; gap: 10px; }
</style>
</head>
<body>

<h2>Addition Quiz</h2>
<p>Answer all questions, then click <b>Submit</b>.</p>

<form action="additionResult.jsp" method="post">
<table>
<%
    for (int i = 0; i < a.length; i++) {
%>
<tr>
<td><%= (i + 1) %>.</td>
<td><%= a[i] %> + <%= b[i] %> =</td>
<td><input type="text" name="ans<%= i %>" /></td>
</tr>
<%
    }
%>
</table>

<div class="actions">
<button class="btn" type="submit">Submit</button>
<a class="btn" href="additionQuiz.jsp?new=1">New Quiz</a>
</div>
</form>

</body>
</html>
