package wjl.mapping.parser;

import wjl.mapping.model.SimplePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 将字符串 ${a[3].b} 解析为SimplePath对象。
 *
 * @author wujinlong
 * @since 2021-8-5
 */
public class SimplePathParser {
    // 包含路径的字符串，和路径的起始、结束位置。
    private String strPath;
    private int start;
    private int end;

    // 一致性约束：ch永远等于ptrCh位置的字符。
    private int ptrCh;
    private char ch;

    // 错误信息
    private String error;

    /**
     * 将字符串 ${a[3].b} 解析为SimplePath对象，${} 代表空路径。
     *
     * @param strPath 必须是 ${...} 格式的字符串
     * @return 返回空表示存在错误
     */
    public SimplePath parse(String strPath) {
        return parse(strPath, 2, strPath.length() - 1);
    }

    /**
     * 将字符串 ${a[3].b} 解析为SimplePath对象，输入为空时返回空路径。
     *
     * @param strPath 包含路径的字符串。
     * @param start 字符 ${ 后面的第一个字符的位置
     * @param end 字符 } 的位置
     * @return 返回空表示存在错误
     */
    public SimplePath parse(String strPath, int start, int end) {
        this.error = null;
        if (strPath == null || start >= end) {
            return SimplePath.EMPTY;
        }

        this.strPath = strPath;
        this.start = start;
        this.end = end;
        ptrCh = start;
        ch = strPath.charAt(ptrCh);

        List<Object> nodeList = new ArrayList<>();
        Object node = firstToken();
        while (node != null) {
            nodeList.add(node);
            node = nextToken();
        }

        if (error != null) {
            return null;
        } else if (ptrCh < end) {
            error = String.format(Locale.ENGLISH, "unsupported char '%c' at %d in '%s'",
                ch, ptrCh - start, strPath.substring(start, end));
            return null;
        }

        return new SimplePath(nodeList.toArray());
    }

    public String getError() {
        return error;
    }

    private Object firstToken() {
        if (ch == '[') {
            return squareBracketToken();
        } else {
            return nameOrIndex();
        }
    }

    private Object nextToken() {
        if (ch == '.') {
            if (ptrCh + 1 == end) {
                return null;
            }
            ch = strPath.charAt(++ptrCh);
            return firstToken();
        } else if (ch == '[') {
            return squareBracketToken();
        } else {
            return null;
        }
    }

    private Object nameOrIndex() {
        int intValue = 0;
        int tokenStart = ptrCh;
        while (isNumber(ch)) {
            intValue = intValue * 10 + ch - '0';
            ch = strPath.charAt(++ptrCh);
        }

        if (isTokenChar(ch)) {
            intValue = -1;
            do {
                ch = strPath.charAt(++ptrCh);
            } while (isTokenChar(ch));
        }

        if (ptrCh > tokenStart) {
            if (intValue >= 0) {
                return intValue;
            } else {
                return strPath.substring(tokenStart, ptrCh);
            }
        }
        return null;
    }

    // 前置条件：ch等于左方括号
    // 结束状态：ch等于右方括号后面的一个字符
    private Object squareBracketToken() {
        ch = strPath.charAt(++ptrCh);
        Object token = nameOrIndex();
        if (ch == ']') {
            if (token != null) {
                ch = strPath.charAt(++ptrCh);
                return token;
            } else {
                error = String.format(Locale.ENGLISH, "empty '[]' at %d in '%s'",
                    ptrCh - start, strPath.substring(start, end));
                return null;
            }
        }

        if (ptrCh == end) {
            error = String.format(Locale.ENGLISH, "require ']' at %d in '%s'",
                ptrCh - start, strPath.substring(start, end));
        } else {
            error = String.format(Locale.ENGLISH, "unsupported char '%c' at %d in '%s'",
                ch, ptrCh - start, strPath.substring(start, end));
        }
        return null;
    }

    private boolean isNumber(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private boolean isTokenChar(char ch) {
        return 'A' <= ch && ch <= 'Z'
            || 'a' <= ch && ch <= 'z'
            || '0' <= ch && ch <= '9'
            || ch == '-'
            || ch == '_';
    }
}
