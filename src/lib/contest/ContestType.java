package lib.contest;

public enum ContestType {
    PLAIN("", -1),
    ETH_JUDGE("", -1, null, false, false, 6),
    GOOGLE("Case #%d: ", -1),
    GOOGLE_JAM("Case #%d: ", -1, "Solution.java", false, false, 8),
    GOOGLE_JAM_INTERACTIVE("", -1, "Solution.java", false, false, 8),
    FACEBOOK("Case #%d: ", -1),
    BLOOMBERG("", 1, "Problem.java", false, false, 8),
    SINGLE_TESTCASE("", 1),
    OPTIMIZER("", 1, null, false, false, 8, true),
    HACKERRANK("", 1, "Solution.java", false, false, 8),
    CODEFORCES("", 1, "Solution.java", false, false, 8);


    public final int testCaseCount;
    public final String caseString;
    public final String launcher;
    public final boolean disableCompression;
    public final boolean keepUnusedDependencies;
    public final int javaVersion;
    public final boolean isOptimizer;

    ContestType(String caseString, int testCaseCount, String launcher, boolean disableCompression, boolean keepUnusedDependencies, int javaVersion, boolean isOptimizer) {
        this.caseString = caseString;
        this.testCaseCount = testCaseCount;
        this.launcher = launcher;
        this.disableCompression = disableCompression;
        this.javaVersion = javaVersion;
        this.keepUnusedDependencies = keepUnusedDependencies;
        this.isOptimizer = isOptimizer;
    }

    ContestType(String a1, int a2, String a3, boolean a4, boolean a5, int a6) { this(a1, a2, a3, a4, a5, a6, false); }
    ContestType(String a1, int a2) {
        this(a1, a2, null, false, false, 8);
    }


}
