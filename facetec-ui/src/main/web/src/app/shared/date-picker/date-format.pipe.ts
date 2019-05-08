import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'dateFormatPipe',
})
export class DateFormatPipe implements PipeTransform {
  transform(value: string) {
    const datePipe = new DatePipe('pt-BR');
    try {
      value = datePipe.transform(value, 'dd/MM/yyyy');
    } catch {
      value = '';
    }
    return value;
  }
}
