package Ex4.entities;

import javax.persistence.*;

    @Entity
    @Table(name = "_04_medicaments")
    public class Medicaments {

        private Long id;
        private String name;

        public Medicaments() {
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Column(name = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

