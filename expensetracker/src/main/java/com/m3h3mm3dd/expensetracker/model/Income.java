package com.m3h3mm3dd.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Income extends MoneyEntry {}
