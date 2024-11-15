package facs.edu.entities;

import facs.edu.entities.enums.TokensTypes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnaliseLexica {
    private final String operators = "[+\\-*/]";
    private final BufferedReader reader;
    private List<Token> tokenList = new LinkedList<>();

    private List<Character> characterList = new LinkedList<>();
    private Integer firstPosition = null;

    public AnaliseLexica(String fileDirectory) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(fileDirectory));
    }

    public void run() throws IOException {
        List<String> lines = reader.lines().toList();
        boolean isOpenString = false;
        int numbesOfAspas = 0;
        int presentLine = 0;
        List<Character> stringFormation = new ArrayList<Character>();

        for (String l : lines) {
            for (int i = 0; i < l.length(); i++) {
                char charAt = l.charAt(i);

                if(charAt == '"' || isOpenString){
                    isOpenString = true;
                    stringFormation.add(charAt);
                    if (numbesOfAspas == 0){
                        this.firstPosition = i;
                    }

                    if (charAt == '"'){
                        numbesOfAspas++;
                    }

                    if (numbesOfAspas == 2){
                        isOpenString = false;
                        validateString(stringFormation, presentLine);
                        System.out.println("STRING");
                    }

                }
                else if (isSpace(charAt, presentLine, i)){
                    System.out.println("SKIP");
                } else if (isInteger(charAt, presentLine, i)) {
                    System.out.println("INTEGER");
                }else if(hasSpecialToken(charAt, presentLine, i)){
                    System.out.println("ARYTHMETICAL | SPECIAL");
                }
                else if (validateString(charAt,presentLine, i, l)) {
                    System.out.println("ID | RESERVED WORD");
                }
            }


            presentLine++;
        }

        tokenList.sort(Comparator.comparing(Token::getTokenPosition));
        this.removeWhiteSpacesFromList();
    }

    private boolean isSpace(Character character, int foundLine, int foundPosition){
        if (character == ' '){
            Token token = new Token(
                    TokensTypes.SKIP,
                    " ",
                    foundLine,
                    foundPosition
            );

            tokenList.add(token);
            return true;
        }

        return false;
    }

    private void validateString(List<Character> sentence, int foundLine) {
        StringBuilder definedString = new StringBuilder();

        for (Character character : sentence){
            definedString.append(character);
        }

        Token token = new Token(
                TokensTypes.STRING,
                definedString.toString(),
                foundLine,
                this.firstPosition
        );
        tokenList.add(token);

    }

    private boolean isInteger(Character character, int foundLine, int foundPosition){
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(character.toString());

        if (matcher.find()){
            Token token = new Token(
                TokensTypes.INTEGER,
                character.toString(),
                foundLine,
                foundPosition
            );


            tokenList.add(token);

            return true;
        }

        return false;
    }

    private boolean validateString(Character character, int foundLine, int foundPosition, String fullLine){
        if (characterList.isEmpty()){
            this.firstPosition = foundPosition;
        }

        characterList.add(character);

        StringBuilder stringToCheck = new StringBuilder();

        for (Character c : characterList){
            stringToCheck.append(c);
        }

        try {
            if (fullLine.charAt(foundPosition + 1) == ' ' || fullLine.charAt(foundPosition + 1) == '=' || fullLine.charAt(foundPosition + 1) == ';'){
                if (isReservedWord(stringToCheck.toString(), foundLine)){
                    return true;
                }else if(isId(stringToCheck.toString(), foundLine)){
                    return  true;
                }
            }else {
                if (isReservedWord(stringToCheck.toString(), foundLine)){
                    return true;
                }
            }
        }catch (StringIndexOutOfBoundsException ignored) {
        }

        return false;
    }

    private boolean isReservedWord(String valueToCheck, int foundLine) {
        String regexKeyWords = "\\b(to|this|or|loop|Print|digint|digdouble|letter)\\b";
        Pattern pattern = Pattern.compile(regexKeyWords);
        Matcher matcher = pattern.matcher(valueToCheck);

        if(matcher.find()){
            Token token = new Token(
                    TokensTypes.valueOf(matcher.group().toUpperCase()),
                    matcher.group(),
                    foundLine,
                    this.firstPosition
            );


            tokenList.add(token);

            this.characterList = new ArrayList<>();
            this.firstPosition = null;
            return true;
        }

        return false;
    };

    private boolean isId(String valueToCheck, int foundLine) {
        Token token = new Token(
                TokensTypes.ID,
                valueToCheck,
                foundLine,
                this.firstPosition
        );

        tokenList.add(token);

        this.characterList = new ArrayList<>();
        this.firstPosition = null;
        return true;
    };

    private boolean hasSpecialToken(Character character, int foundLine, int foundPosition){
        boolean functionReturn = false;
        Token token = null;
        switch (character){
            case '(':
                token = new Token(
                        TokensTypes.LPAREN,
                        "(",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case ')':
                token = new Token(
                        TokensTypes.RPAREN,
                        ")",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case '{':
                token = new Token(
                        TokensTypes.RKEY,
                        "{",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case '}':
                token = new Token(
                        TokensTypes.LKEY,
                        "}",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case '+':
                token = new Token(
                        TokensTypes.OPERATOR_PLUS,
                        "+",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
                break;
            case '-':
                token = new Token(
                        TokensTypes.OPERATOR_MINUS,
                        "-",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
                break;
            case '*':
                token = new Token(
                        TokensTypes.OPERATOR_MULTIPLICATE,
                        "*",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
                break;
            case '/':
                token = new Token(
                        TokensTypes.OPERATOR_DIVISION,
                        "/",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
                break;
            case ';':
                token = new Token(
                        TokensTypes.END_LINE,
                        ";",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
                break;
            case '.':
                token = new Token(
                        TokensTypes.FLOAT,
                        ".",
                        foundLine,
                        foundPosition
                );
                functionReturn =true;
            default:
                    break;
        }

        if (functionReturn){
            tokenList.add(token);
        }

        return functionReturn;
    }

    private void removeWhiteSpacesFromList(){
        this.tokenList = this.tokenList.stream().filter(token -> token.getTokenType() != TokensTypes.SKIP).toList();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        for (Token token: this.tokenList){
            stringBuilder.append(token + "\n");
        }

        return  stringBuilder.toString();
    }
}
