package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.CombustivelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CombustivelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Combustivel.class);
        Combustivel combustivel1 = getCombustivelSample1();
        Combustivel combustivel2 = new Combustivel();
        assertThat(combustivel1).isNotEqualTo(combustivel2);

        combustivel2.setId(combustivel1.getId());
        assertThat(combustivel1).isEqualTo(combustivel2);

        combustivel2 = getCombustivelSample2();
        assertThat(combustivel1).isNotEqualTo(combustivel2);
    }
}
