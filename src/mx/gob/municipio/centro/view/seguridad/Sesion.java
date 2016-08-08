/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.seguridad;

public class Sesion {
	private String usuario;
    private int idUsuario;
    private String  unidad;
    private String idUnidad;
    private String  host;
    private String  ip;
    private int ejercicio;
    private String  claveUnidad;
    private Integer  idGrupo; 
    


    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }



    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setIdUnidad(String idUnidad) {
		this.idUnidad = idUnidad;
	}

	public String getIdUnidad() {
		return idUnidad;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public int getEjercicio() {
		return ejercicio;
	}

	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}
}
