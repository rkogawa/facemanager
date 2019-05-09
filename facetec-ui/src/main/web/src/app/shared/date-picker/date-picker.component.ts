import { Component, Input, forwardRef, LOCALE_ID, AfterContentInit, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor, Validator, NG_VALIDATORS, AbstractControl, ValidationErrors } from '@angular/forms';
import localePt from '@angular/common/locales/pt';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localePt);

const noop = () => {
};

@Component({
  selector: 'app-date-picker',
  templateUrl: './date-picker.component.html',
  styleUrls: ['./date-picker.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DatePickerComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => DatePickerComponent),
      multi: true,
    },
    { provide: LOCALE_ID, useValue: 'pt-BR' }
  ]
})
export class DatePickerComponent implements ControlValueAccessor, AfterContentInit, Validator {
  public mask = {
    guide: true,
    showMask: false,
    mask: [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/]
  };

  @Input() placeholder = '';
  @Input() disabled = false;
  @Input() required = false;
  @ViewChild('inputComponent') inputComponent;

  innerValue = '';

  private onTouchedCallback: () => void = noop;
  private onChangeCallback: (_: any) => void = noop;

  ngAfterContentInit() {
    this.placeholder = this.required ? this.placeholder + ' *' : this.placeholder;
  }

  get value(): string {
    return this.innerValue;
  }

  set value(v: string) {
    if (v !== undefined) {
      const data = new Date(v);
      this.setInnerValue(data.toISOString().split('T')[0]);
    }
  }

  setInnerValue(value: string) {
    this.innerValue = value;
    this.onChangeCallback(this.innerValue);
  }

  writeValue(value: any): void {
    if (value && value !== this.innerValue) {
      this.innerValue = value;
    } else {
      this.innerValue = '';
    }
  }

  registerOnChange(fn: any): void {
    this.onChangeCallback = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouchedCallback = fn;
  }

  todate(value) {
    if (value === '__/__/____') {
      this.setInnerValue('');
    } else {
      const data = value.split('/');
      this.setInnerValue(`${data[2]}-${data[1]}-${data[0]}`);
    }
  }

  onBlur() {
    this.onChangeCallback(this.innerValue);
  }

  validate(control: AbstractControl): ValidationErrors {
    return (!this.required || this.validaData()) ? null : {
      dataError: { valid: false },
    };
  }

  validaData() {
    try {
      const dataArray = this.innerValue.split('-');
      const data = new Date(Number(dataArray[0]), Number(dataArray[1]) + 1, Number(dataArray[2]));
      if (isNaN(data.getTime())) {
        return false;
      }
      return true;
    } catch {
      return false;
    }
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
