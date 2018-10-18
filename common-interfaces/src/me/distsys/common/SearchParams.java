package me.distsys.common;

import java.io.Serializable;

public class SearchParams implements Serializable {
    private static final long serialVersionUID = 1L;
    public String hotel;
    public String dataEntrada;
    public String dataSaida;
    public int numeroQuartos;
    public int numeroPessoas;
    public boolean idaEVolta;
    public String origem;
    public String destino;
    public String dataIda;
    public String dataVolta;
    public int preço;
}
