package bg.sofia.uni.fmi.mjt.stylechecker;

enum FixMeMessages {
    multipleStatementsError("// FIXME Only one statement per line is allowed"),
    wildcardsError("// FIXME Wildcards are not allowed in import statements"),
    sameLineBracketsError("// FIXME Opening brackets should be placed on the same line as the declaration"),
    tooLargeLineError("// FIXME Length of line should not exceed 100 characters");

    private String message;

    FixMeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}