import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-dish-form',
  templateUrl: './dish-form.component.html',
  styleUrls: ['./dish-form.component.css']
})
export class DishFormComponent implements OnInit {
  dishForm!: FormGroup;
  isSubmitting = false;
  submitErrorMessage: string | null = null;
  editMode = false;
  dishId?: number;

  constructor(
    private fb: FormBuilder,
    private dishService: DishService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.dishForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(0)]],
      ingredients: ['', Validators.required],
      vegan: [false],
      imageField: [null]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.editMode = true;
        this.dishId = +id;
        this.loadDish(this.dishId);
      }
    });
  }

  loadDish(id: number): void {
    this.dishService.getDishById(id).subscribe({
      next: (dish) => {
        this.dishForm.patchValue(dish);
      },
      error: (err) => {
        console.error('Error loading dish:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.dishForm.invalid) {
      this.dishForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submitErrorMessage = null;

    const dishData: DishDTO = {
      ...this.dishForm.value
    };

    const request$ = this.editMode && this.dishId
      ? this.dishService.updateDish(this.dishId, dishData)
      : this.dishService.addDish(dishData);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/dishes']);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.submitErrorMessage = err.error?.message || 'Error while saving the dish.';
      }
    });
  }

  previewImage: string | ArrayBuffer | null = null;

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result;
      };
      reader.readAsDataURL(file);
      this.dishForm.patchValue({ imageField: file });
    }
  }


  // Getters para validaciones en template
  get name() { return this.dishForm.get('name'); }
  get price() { return this.dishForm.get('price'); }
  get ingredients() { return this.dishForm.get('ingredients'); }
}
