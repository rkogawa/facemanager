import { Directive, ElementRef, Renderer, Input } from "@angular/core";
import { NG_VALUE_ACCESSOR } from "@angular/forms";

@Directive({
    selector: "[async-btn]",
    exportAs: "async-btn"
})
export class AsyncButtonDirective {

    @Input() loadingLabel = 'Carregando';

    btnLabel;

    waiting: boolean;

    constructor(private renderer: Renderer, private el: ElementRef) {
    }

    wait() {
        this.waiting = true;
        this.btnLabel = this.el.nativeElement.innerHTML;
        this.el.nativeElement.textContent = '';
        let icon = this.renderer.createElement(this.el.nativeElement, 'i');
        this.renderer.setElementClass(icon, 'fa', true);
        this.renderer.setElementClass(icon, 'fa-spinner', true);
        this.renderer.setElementClass(icon, 'fa-spin', true);
        this.renderer.createText(this.el.nativeElement, ' ' + this.loadingLabel);
    }

    release() {
        if (this.btnLabel) {
            this.el.nativeElement.textContent = '';
            this.el.nativeElement.innerHTML = this.btnLabel;
        }
        this.waiting = false;
    }
}