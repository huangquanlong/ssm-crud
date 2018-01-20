package com.yuchengtech.bob.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "to_be_delete")
public class CommonSequence {

    @Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="CommonSequnce")
    @TableGenerator(name="CommonSequnce",allocationSize=1, table="sequence",pkColumnName="seq_name",valueColumnName="seq_value", pkColumnValue="id_sequence")
    protected Long id;

}
