package wjl.cli;

import org.apache.commons.lang3.StringUtils;

public class InvalidCommandLine extends RuntimeException {
    public InvalidCommandLine(String...cfg) {
        super(StringUtils.join(cfg, ' '));
    }
}
