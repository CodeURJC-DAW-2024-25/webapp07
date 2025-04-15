import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent {
  @Input() title: string = 'Confirmation';
  @Input() message: string = 'Are you sure you want to save these changes?';
  @Input() showModal: boolean = false;
  @Output() confirmed = new EventEmitter<boolean>();
  @Output() closed = new EventEmitter<void>();
  @Output() closedCompletely = new EventEmitter<void>();

  confirm() {
    this.confirmed.emit(true);
    this.showModal = false;
    setTimeout(() => {
      this.closedCompletely.emit();
    }, 100);
  }

  cancel() {
    this.closed.emit();
    this.showModal = false;
    setTimeout(() => {
      this.closedCompletely.emit();
    }, 100);
  }
}
