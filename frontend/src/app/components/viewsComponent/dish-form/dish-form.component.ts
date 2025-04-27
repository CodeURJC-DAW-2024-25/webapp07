//dish-form-component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import { ActivatedRoute, Router } from '@angular/router';
import {of} from "rxjs";
import { Allergens } from "../../../dtos/enums/allergens.enum";

@Component({
  selector: 'app-dish-form',
  templateUrl: './dish-form.component.html',
  styleUrls: ['./dish-form.component.css']
})
export class DishFormComponent implements OnInit {
  dish!: DishDTO;
  dishForm!: FormGroup;
  isSubmitting = false;
  submitErrorMessage: string | null = null;
  editMode = false;
  dishId?: number;
  previewImage: string | ArrayBuffer | null = null;

  allAllergens = [
    { name: 'GLUTEN', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_gluten.png' },
    { name: 'CRUSTACEANS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_crustacean.png' },
    { name: 'EGGS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_eggs.png' },
    { name: 'FISH', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_fish.png' },
    { name: 'PEANUTS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_peanut.png' },
    { name: 'SOYBEANS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_soybeans.png' },
    { name: 'NUTS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_nuts.png' },
    { name: 'CELERY', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_cereal.png' },
    { name: 'MUSTARD', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_mustard.png' },
    { name: 'SESAME', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_sesame.png' },
    { name: 'SULPHITES', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_sulfites.png' },
    { name: 'MOLLUSCS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_mollusks.png' }
  ];

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
      isVegan: [false],
      allergens: [[]],
      imageField: [null]
    });

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');

      // Sólo si idParam es un entero válido:
      const idNum = idParam !== null ? Number(idParam) : NaN;
      if (idParam !== null && !isNaN(idNum)) {
        this.editMode = true;
        this.dishId   = idNum;
        this.loadDish(idNum);
      } else {
        // Ruta de creación: no cargamos nada
        this.editMode = false;
        this.dishId   = undefined;
      }
    });
  }

  loadDish(id: number): void {
    this.dishService.getDishById(id).subscribe({
      next: (dish) => {
        this.dish = dish;
        this.dishForm.patchValue({
          ...dish,
          ingredients: dish.ingredients.join(', '),
          allergens: dish.allergens ?? []
        });
      },
      error: (err) => {
        console.error('Error loading dish:', err);
      }
    });
  }

  // onSubmit(): void {
  //   if (this.dishForm.invalid) {
  //     this.dishForm.markAllAsTouched();
  //     return;
  //   }
  //
  //   this.isSubmitting = true;
  //   this.submitErrorMessage = null;
  //
  //   const fv = this.dishForm.value as {
  //     name: string;
  //     description: string;
  //     price: number;
  //     ingredients: string;
  //     allergens: string[];
  //     isVegan: boolean;
  //     imageField: File | null;
  //   };
  //
  //   const file = fv.imageField;
  //
  //   const selectedAllergens: Allergens[] = (fv.allergens ?? [])
  //     .map(name => name as keyof typeof Allergens)
  //     .map(key => Allergens[key]);
  //
  //   const hasNewImage = !!file;
  //
  //   let dtoBase = {
  //     name: fv.name,
  //     description: fv.description,
  //     price: fv.price,
  //     ingredients: fv.ingredients.split(',').map(i => i.trim()),
  //     allergens: selectedAllergens,
  //     isVegan: fv.isVegan,
  //     available: this.dish?.available ?? true,
  //     rates: this.dish?.rates ?? [],
  //     rate: this.dish?.rate ?? 0,
  //     dishImagePath: this.dish?.dishImagePath ?? '',
  //   };
  //
  //   let dto: DishDTO | Omit<DishDTO, 'id'>;
  //
  //   if (this.editMode && this.dishId != null) {
  //     dto = {
  //       id: this.dishId,
  //       ...dtoBase,
  //       image: hasNewImage
  //     };
  //   } else {
  //     dto = {
  //       ...dtoBase,
  //       image: hasNewImage
  //     };
  //   }
  //
  //
  //   if (this.editMode && this.dishId != null) {
  //     // UPDATE
  //     this.dishService.updateDish(this.dishId, dto).subscribe({
  //       next: () => {
  //         const img$ = file
  //           ? this.dishService.replaceDishImage(this.dishId!, file)
  //           : of(void 0);
  //
  //         img$.subscribe({
  //           next: () => this.finishSubmit(),
  //           error: err => this.handleError(err)
  //         });
  //       },
  //       error: err => this.handleError(err)
  //     });
  //
  //   } else {
  //     // CREATE
  //     this.dishService.addDish(dto).subscribe({
  //       next: response => {
  //         const location = response.headers.get('Location');
  //         if (!location) {
  //           this.handleError(new Error('No Location header received'));
  //           return;
  //         }
  //
  //         const idString = location.split('/').pop();
  //         const newDishId = idString ? Number(idString) : NaN;
  //
  //         if (isNaN(newDishId)) {
  //           this.handleError(new Error(`Invalid dish ID parsed from Location: ${idString}`));
  //           return;
  //         }
  //
  //         const upload$ = file
  //           ? this.dishService.uploadDishImage(newDishId, file)
  //           : of(void 0);
  //
  //         upload$.subscribe({
  //           next: () => this.finishSubmit(),
  //           error: err => this.handleError(err)
  //         });
  //       },
  //       error: err => this.handleError(err)
  //     });
  //   }
  // }
  onSubmit(): void {
    if (this.dishForm.invalid) {
      this.dishForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submitErrorMessage = null;

    const fv = this.dishForm.value as {
      name: string;
      description: string;
      price: number;
      ingredients: string;
      allergens: string[];
      isVegan: boolean;
      imageField: File | null;
    };

    const file = fv.imageField;

    const selectedAllergens: Allergens[] = (fv.allergens ?? [])
      .map(name => name as keyof typeof Allergens)
      .map(key => Allergens[key]);

    const hasNewImage = !!file;

    let dto: DishDTO | Omit<DishDTO, 'id'>;

    if (this.editMode && this.dishId != null && this.dish) {
      dto = {
        ...this.dish, // ← Copiamos todo lo anterior
        name: fv.name,
        description: fv.description,
        price: fv.price,
        ingredients: fv.ingredients.split(',').map(i => i.trim()),
        allergens: selectedAllergens,
        isVegan: fv.isVegan,
        image: hasNewImage // Solo true/false para indicar si hay imagen nueva
      };
    } else {
      dto = {
        name: fv.name,
        description: fv.description,
        price: fv.price,
        ingredients: fv.ingredients.split(',').map(i => i.trim()),
        allergens: selectedAllergens,
        isVegan: fv.isVegan,
        available: true,
        rates: [],
        rate: 0,
        dishImagePath: '',
        image: hasNewImage
      };
    }

    if (this.editMode && this.dishId != null) {
      // UPDATE
      this.dishService.updateDish(this.dishId, dto).subscribe({
        next: () => {
          const img$ = file
            ? this.dishService.replaceDishImage(this.dishId!, file)
            : of(void 0);

          img$.subscribe({
            next: () => this.finishSubmit(),
            error: err => this.handleError(err)
          });
        },
        error: err => this.handleError(err)
      });
    } else {
      // CREATE
      this.dishService.addDish(dto).subscribe({
        next: response => {
          const location = response.headers.get('Location');
          if (!location) {
            this.handleError(new Error('No Location header received'));
            return;
          }

          const idString = location.split('/').pop();
          const newDishId = idString ? Number(idString) : NaN;

          if (isNaN(newDishId)) {
            this.handleError(new Error(`Invalid dish ID parsed from Location: ${idString}`));
            return;
          }

          const upload$ = file
            ? this.dishService.uploadDishImage(newDishId, file)
            : of(void 0);

          upload$.subscribe({
            next: () => this.finishSubmit(),
            error: err => this.handleError(err)
          });
        },
        error: err => this.handleError(err)
      });
    }
  }



  private finishSubmit() {
    this.isSubmitting = false;
    this.router.navigate(['/dishes']);
  }

  private handleError(err: any) {
    this.isSubmitting = false;
    this.submitErrorMessage = err.error?.message || 'Error al guardar el plato.';
  }

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

  onAllergenChange(event: any) {
    const selectedAllergens: string[] = this.dishForm.value.allergens;
    const allergen = event.target.value;

    if (event.target.checked) {
      this.dishForm.patchValue({ allergens: [...selectedAllergens, allergen] });
    } else {
      this.dishForm.patchValue({
        allergens: selectedAllergens.filter(a => a !== allergen)
      });
    }
  }

  // Getters para validaciones en template
  get name() { return this.dishForm.get('name'); }
  get price() { return this.dishForm.get('price'); }
  get ingredients() { return this.dishForm.get('ingredients'); }
  get isVegan() { return this.dishForm.get('isVegan'); }
  get description() { return this.dishForm.get('description'); }
  get image() { return this.dishForm.get('image'); }
}
