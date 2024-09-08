import { AfterViewInit, Component, ElementRef, OnInit, inject, signal, viewChild } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { IUsuarioInstituicao } from 'app/entities/usuario-instituicao/usuario-instituicao.model';
import { LoginService } from 'app/login/login.service';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-login',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
})
export default class LoginComponent implements OnInit, AfterViewInit {
  username = viewChild.required<ElementRef>('username');

  authenticationError = signal(false);

  step = 1;
  instituicoesList: IUsuarioInstituicao[] = [];

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    usuarioInstituicao: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });

  private accountService = inject(AccountService);
  private loginService = inject(LoginService);
  private router = inject(Router);
  private readonly stateStorageService = inject(StateStorageService);

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.username().nativeElement.focus();
  }

  // eslint-disable-next-line @typescript-eslint/no-redundant-type-constituents
  compare(o1: any | null, o2: any | null): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  validarLogin(): void {
    this.loginService.listarInstiuicoes(this.loginForm.getRawValue().username).subscribe(instituicoes => {
      this.instituicoesList = instituicoes.body ?? [];
      if (this.instituicoesList.length > 0) {
        this.step++;
      } else {
        this.authenticationError.set(true);
      }
    });
  }

  login(): void {
    const authentication = this.loginForm.getRawValue() as any;
    authentication.usuarioInstituicao = authentication.usuarioInstituicao.id;
    this.loginService.login(authentication).subscribe({
      next: () => {
        this.authenticationError.set(false);
        this.stateStorageService.setInstituicao(this.loginForm.getRawValue().usuarioInstituicao);
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['']);
        }
      },
      error: () => this.authenticationError.set(true),
    });
  }
}
