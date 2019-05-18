import {
  Component,
  OnInit,
  Input,
  forwardRef,
  ViewChild,
  AfterContentInit,
  EventEmitter,
  Output
} from '@angular/core';
import {
  FormControl,
  ControlValueAccessor,
  NG_VALUE_ACCESSOR,
  NG_VALIDATORS,
  Validator,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

import { MatAutocompleteTrigger } from '@angular/material';
import { FacetecService } from '../../services/facetec.service';


@Component({
  selector: 'app-autocomplete',
  templateUrl: './autocomplete.component.html',
  styleUrls: ['./autocomplete.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AutocompleteComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => AutocompleteComponent),
      multi: true,
    }
  ]
})
export class AutocompleteComponent implements OnInit, ControlValueAccessor, Validator, AfterContentInit {

  @ViewChild(MatAutocompleteTrigger) trigger;

  @Input() path: string = null;
  @Input() placeholder: string = null;
  @Input() disableClean = false;
  @Input() optionsList: Array<string>;
  @Output() onChangeEvent = new EventEmitter<boolean>();

  myControl: FormControl = new FormControl('');
  options = new Array<string>();
  filteredOptions: Observable<string[]>;

  constructor(private service: FacetecService) { }

  ngAfterContentInit() {
    // this.placeholder = this.notRequired === null ? this.placeholder + ' *' : this.placeholder;
  }

  ngOnInit() {
    if (this.optionsList !== undefined) {
      this.options = this.optionsList;
    } else {
      this.loadList();
    }

    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );
  }

  loadList() {
    this.service.get<string[]>(`${this.path}/list`).subscribe(resp => {
      this.options = resp;
      if (this.myControl.value) { this.propagateChange(this.myControl.value); }
    });
  }

  reloadWith(list: string[]) {
    this.options = list;
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    if (this.options) {
      return this.options.filter(option => option.toLowerCase().includes(filterValue));
    }
  }

  public onSelectionChanged(value) {
    this.myControl.setValue(value);
    this.onChange();
  }

  onChange() {
    this.propagateChange(this.myControl.value);
    this.onChangeEvent.emit(true);
  }

  onFocus() {
    this.trigger._onChange('');
    this.trigger.openPanel();
  }

  propagateChange = (_: any) => { };

  writeValue(value: any): void {
    if (value !== undefined && value !== null) {
      this.myControl.setValue(value);
    }
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any): void { }

  validate(control: AbstractControl): ValidationErrors {
    return (this.validaValor(this.myControl.value)) ? null : {
      selecaoError: { valid: false },
    };
  }

  validaValor(valor: string) {
    if (valor === null || valor.length === 0) {
      return true;
    }

    if (this.options) {
      return this.options.includes(valor);
    }
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.myControl.disable();
      this.disableClean = true;
    } else {
      this.myControl.enable();
      this.disableClean = false;
    }
  }
}
