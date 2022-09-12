package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "subject_dept")
public class SubjectDept {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject_id")
    private int subjectId;

    @Column(name = "dept_id")
    private int deptId;


    public SubjectDept(int subjectId, int deptId) {
        this.subjectId = subjectId;
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return "SubjectDept{" +
                "id=" + id +
                ", subjectId=" + subjectId +
                ", deptId=" + deptId +
                '}';
    }
}
