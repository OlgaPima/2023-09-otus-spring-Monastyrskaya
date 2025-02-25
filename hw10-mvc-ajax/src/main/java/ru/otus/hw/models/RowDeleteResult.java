package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// аж целый класс с одним полем - это чтобы автоматически сериализовывалось в объект json
public class RowDeleteResult {
    boolean success;
}
