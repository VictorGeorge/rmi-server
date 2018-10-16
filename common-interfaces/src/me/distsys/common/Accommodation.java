package me.distsys.common;

import java.io.Serializable;

public class Accommodation implements Serializable {

    private int id;
    private String hotel;
    private String dataEntrada;
    private String dataSaida;
    private int numeroQuartos;
    private int numeroPessoas;
    private int precoPorQuarto;
    private int precoPorPessoa;
    private int valorTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public int getNumeroQuartos() {
        return numeroQuartos;
    }

    public void setNumeroQuartos(int numeroQuartos) {
        this.numeroQuartos = numeroQuartos;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(int numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
    }

    public int getPrecoPorQuarto() {
        return precoPorQuarto;
    }

    public void setPrecoPorQuarto(int precoPorQuarto) {
        this.precoPorQuarto = precoPorQuarto;
    }

    public int getPrecoPorPessoa() {
        return precoPorPessoa;
    }

    public void setPrecoPorPessoa(int precoPorPessoa) {
        this.precoPorPessoa = precoPorPessoa;
    }

    public int getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(int valorTotal) {
        this.valorTotal = valorTotal;
    }

}

