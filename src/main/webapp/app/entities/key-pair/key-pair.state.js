(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('key-pair', {
            parent: 'entity',
            url: '/key-pair?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KeyPairs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/key-pair/key-pairs.html',
                    controller: 'KeyPairController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('key-pair-detail', {
            parent: 'key-pair',
            url: '/key-pair/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'KeyPair'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/key-pair/key-pair-detail.html',
                    controller: 'KeyPairDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'KeyPair', function($stateParams, KeyPair) {
                    return KeyPair.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'key-pair',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('key-pair-detail.edit', {
            parent: 'key-pair-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-pair/key-pair-dialog.html',
                    controller: 'KeyPairDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeyPair', function(KeyPair) {
                            return KeyPair.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('key-pair.new', {
            parent: 'key-pair',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-pair/key-pair-dialog.html',
                    controller: 'KeyPairDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pairType: null,
                                keyName: null,
                                keyValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('key-pair', null, { reload: 'key-pair' });
                }, function() {
                    $state.go('key-pair');
                });
            }]
        })
        .state('key-pair.edit', {
            parent: 'key-pair',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-pair/key-pair-dialog.html',
                    controller: 'KeyPairDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeyPair', function(KeyPair) {
                            return KeyPair.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('key-pair', null, { reload: 'key-pair' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('key-pair.delete', {
            parent: 'key-pair',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-pair/key-pair-delete-dialog.html',
                    controller: 'KeyPairDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['KeyPair', function(KeyPair) {
                            return KeyPair.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('key-pair', null, { reload: 'key-pair' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
