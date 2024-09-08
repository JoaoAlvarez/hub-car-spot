package com.hubcarspot.api.domain;

import static com.hubcarspot.api.domain.VeiculoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hubcarspot.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VeiculoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Veiculo.class);
        Veiculo veiculo1 = getVeiculoSample1();
        Veiculo veiculo2 = new Veiculo();
        assertThat(veiculo1).isNotEqualTo(veiculo2);

        veiculo2.setId(veiculo1.getId());
        assertThat(veiculo1).isEqualTo(veiculo2);

        veiculo2 = getVeiculoSample2();
        assertThat(veiculo1).isNotEqualTo(veiculo2);
    }
}
