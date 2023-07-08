package org.bot.models.model2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rain {
    private double h3;

    public double getH3() {
        return h3;
    }

    public void setH3(double h3) {
        this.h3 = h3;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "h3=" + h3 +
                '}';
    }
}
