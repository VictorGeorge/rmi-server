package me.distsys.common;

import java.io.Serializable;

public class Flight implements Serializable {
    private String origem;
    private String destino;
    private String data;
    private int vagas;
    private int precoUnitario;

    public Flight(String origem, String destino, String data, int vagas, int precoUnitario) {
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.vagas = vagas;
        this.precoUnitario = precoUnitario;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public int getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(int precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
