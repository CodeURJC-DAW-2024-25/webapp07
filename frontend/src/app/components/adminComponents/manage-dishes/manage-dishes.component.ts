import { Component } from '@angular/core';
import {UserDTO} from "../../../dtos/user.model";
import {DishService} from "../../../services/dish.service";
import {ToastrService} from "ngx-toastr";
import {DishDTO} from "../../../dtos/dish.model";

@Component({
  selector: 'app-manage-dishes',
  templateUrl: './manage-dishes.component.html',
  styleUrl: './manage-dishes.component.css'
})
export class AdminManageDishesComponent {
  public dishes: DishDTO[] = [];
  public isLoading = true;
  public searchQuery: string = '';

  public showConfirmationModal = false;
  public modalTitle = '';
  public modalMessage = '';
  public selectedDish: DishDTO | null = null;
  public actionType: 'ENABLE' | 'DISABLE'  | null = null;

  private pendingToastMessage: { type: 'success' | 'error', message: string, title: string } | null = null;



  constructor(
    private dishesService: DishService,
    protected toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadDishes();
  }

  loadDishes(): void {
    this.isLoading = true;
    this.dishesService.getAllDishes(this.searchQuery).subscribe({
      next: (data) => {
        this.dishes = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading dishes:', err);
        this.isLoading = false;
      }
    });
  }

  confirmEnable(dish: DishDTO): void {
    this.selectedDish = dish;
    this.actionType = 'ENABLE';
    this.modalTitle = 'Confirm enable';
    this.modalMessage = `Are you sure you want to enable dish "${dish.name}"?`;
    this.showConfirmationModal = true;
  }

  confirmDisable(dish: DishDTO): void {
    this.selectedDish = dish;
    this.actionType = 'DISABLE';
    this.modalTitle = 'Confirm disable';
    this.modalMessage = `Are you sure you want to disable dish "${dish.name}"?`;
    this.showConfirmationModal = true;
  }

  onActionConfirmed(): void {
    if (!this.selectedDish || !this.actionType) return;

    const dishId = this.selectedDish.id;
    let action$;


    switch (this.actionType) {
      case 'ENABLE':
        action$ = this.dishesService.enableDish(dishId);
        break;
      case 'DISABLE':
        action$ = this.dishesService.disableDish(dishId);
        break;
      default:
        console.error(`Unknown action type: ${this.actionType}`);
        return;
    }

    action$.subscribe({
      next: () => {
        this.loadDishes();
        this.pendingToastMessage = {
          type: 'success',
          message:
            this.actionType === 'DISABLE'
              ? 'Dish disable successfully!'
              : this.actionType === 'ENABLE'
                ? 'Dish enable successfully!'
                : 'Dish disable successfully!',
          title: 'Success'
        };
        this.resetModalState();
      },
      error: (err) => {
        console.error(`Error performing ${this.actionType} action:`, err);
        this.pendingToastMessage = {
          type: 'error',
          message:
            this.actionType === 'DISABLE'
              ? 'Error disabling dish. Please try again.'
              : this.actionType === 'ENABLE'
                ? 'Error enabling dish. Please try again.'
                : 'Error disabling dish. Please try again.',
          title: 'Error'
        };
        this.resetModalState();
      }
    });

  }


  onModalFullyClosed(): void {
    if (this.pendingToastMessage) {
      const { type, message, title } = this.pendingToastMessage;
      type === 'success'
        ? this.toastr.success(message, title)
        : this.toastr.error(message, title);

      this.pendingToastMessage = null;
    }
  }


  resetModalState(): void {
    this.showConfirmationModal = false;
    this.modalTitle = '';
    this.modalMessage = '';
    this.selectedDish = null;
    this.actionType = null;
  }
}
