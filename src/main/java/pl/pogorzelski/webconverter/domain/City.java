package pl.pogorzelski.webconverter.domain;

public class City implements Comparable<City> {
    private Long id;
    private String name;
    private State state;

    public City() {
        this.name = "unknown";
        this.state = new State("unknown");
        this.id = System.currentTimeMillis();
    }

    public City(String name) {
        this.name = name;
        this.state = new State("unknown");
        this.id = System.currentTimeMillis();
    }

    public int compareTo(City o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        City other = (City) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.state == null) {
            if (other.state != null) {
                return false;
            }
        } else if (!this.state.equals(other.state)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.state == null) ? 0 : this.state.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("City [ ");
        sb.append("id=").append(this.id).append(',');
        sb.append("name=").append(this.name).append(' ');
        sb.append("state=").append(this.state.getName()).append(' ');
        sb.append("]");
        return sb.toString();
    }

}